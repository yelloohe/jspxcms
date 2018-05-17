package com.jspxcms.core.fulltext;

import com.jspxcms.common.fulltext.LuceneIndexTemplate;
import com.jspxcms.common.orm.Limitable;
import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.Node;
import com.jspxcms.core.service.InfoQueryService;
import com.jspxcms.core.service.TaskService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.highlight.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * InfoFulltextServiceImpl
 *
 * @author liufang
 */
@Service
@Transactional(readOnly = true)
public class InfoFulltextServiceImpl implements InfoFulltextService {
    public List<Info> list(Integer[] siteIds, Integer[] nodeIds,
                           Integer[] attrIds, Date beginDate, Date endDate, String[] status,
                           Integer[] excludeId, String q, String title, String[] keywords,
                           String description, String text, String[] creators,
                           String[] authors, Integer fragmentSize, Limitable limitable,
                           Sort sort) {
        Query query = FInfo.query(analyzer, siteIds, nodeIds, attrIds,
                beginDate, endDate, status, excludeId, q, title, keywords,
                description, text, creators, authors);
        List<String> idList = template.list(query, FInfo.ID, limitable, sort);
        List<Integer> ids = FInfo.idsFromString(idList);
        List<Info> list;
        if (!ids.isEmpty()) {
            List<Info> temp = service.findAll(ids);
            list = new ArrayList<Info>(temp.size());
            for (int i = 0, len = ids.size(); i < len; i++) {
                for (Info info : temp) {
                    if (info.getId().equals(ids.get(i))) {
                        list.add(info);
                        break;
                    }
                }
            }
        } else {
            list = Collections.emptyList();
        }

        // 高亮代码
        Query hlQuery = FInfo.query(analyzer, null, null, null, null, null,
                null, null, q, title, keywords, description, text, null, null);
        highlight(hlQuery, fragmentSize, list);

        return list;
        // Predicate predicate = FInfo.query(siteIds, nodeIds, attrIds,
        // beginDate,
        // endDate, status, excludeId, q, title, keywords, description,
        // text);
        // List<Document> docList = template.list(predicate, limitable);
        // List<Integer> ids = FInfo.idsFromDoc(docList);
        // List<Info> list;
        // if (!ids.isEmpty()) {
        // list = service.findAll(ids);
        // } else {
        // list = Collections.emptyList();
        // }
        // return list;
    }

    public Page<Info> page(Integer[] siteIds, Integer[] nodeIds,
                           Integer[] attrIds, Date beginDate, Date endDate, String[] status,
                           Integer[] excludeId, String q, String title, String[] keywords,
                           String description, String text, String[] creators,
                           String[] authors, Integer fragmentSize, Pageable pageable, Sort sort) {
        Query query = FInfo.query(analyzer, siteIds, nodeIds, attrIds,
                beginDate, endDate, status, excludeId, q, title, keywords,
                description, text, creators, authors);
        Page<String> idPage = template.page(query, FInfo.ID, pageable, sort);
        List<Integer> ids = FInfo.idsFromString(idPage.getContent());
        List<Info> content;
        if (!ids.isEmpty()) {
            List<Info> temp = service.findAll(ids);
            content = new ArrayList<Info>(temp.size());
            for (int i = 0, len = ids.size(); i < len; i++) {
                for (Info info : temp) {
                    if (info.getId().equals(ids.get(i))) {
                        content.add(info);
                        break;
                    }
                }
            }
        } else {
            content = Collections.emptyList();
        }

        // 高亮代码
        Query hlQuery = FInfo.query(analyzer, null, null, null, null, null,
                null, null, q, title, keywords, description, text, null, null);
        highlight(hlQuery, fragmentSize, content);

        return new PageImpl<Info>(content, pageable, idPage.getTotalElements());
    }

    private void highlight(Query query, Integer fragmentSize,
                           Collection<Info> coll) {
        if (CollectionUtils.isEmpty(coll)) {
            return;
        }
        if (fragmentSize == null || fragmentSize < 1) {
            fragmentSize = 100;
        }
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(
                "<em class=\"term\">", "</em>");
        Highlighter hl = new Highlighter(formatter, new QueryScorer(query));
        Fragmenter fragmenter = new SimpleFragmenter(fragmentSize);
        hl.setTextFragmenter(fragmenter);
        String str;
        TokenStream ts;
        String hls;
        try {
            for (Info info : coll) {
                str = info.getFullTitleOrTitle();
                ts = analyzer.tokenStream("title", new StringReader(str));
                hls = hl.getBestFragment(ts, str);
                info.setHighlightTitle(hls);

                str = info.getPlainText();
                if (StringUtils.isNotBlank(str)) {
                    ts = analyzer.tokenStream("text", new StringReader(str));
                    hls = hl.getBestFragment(ts, str);
                    info.setHighlightText(hls);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidTokenOffsetsException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDocument(Integer infoId) {
        Info bean = service.get(infoId);
        if (bean != null) {
            template.addDocument(FInfo.doc(bean));
        }
    }

    public void updateDocument(Integer infoId) {
        Info bean = service.get(infoId);
        if (bean != null) {
            template.updateDocument(FInfo.id(infoId), FInfo.doc(bean));
        }
    }

    @Transactional
    public int addDocument(Integer siteId, Node node, TaskService taskService,
                           Integer taskId) {
        Integer[] siteIds = new Integer[]{siteId};
        Integer[] nodeIds = null;
        String treeNumber = null;
        if (node != null) {
            nodeIds = new Integer[]{node.getId()};
            treeNumber = node.getTreeNumber();
        }
        Query query = FInfo.query(analyzer, siteIds, nodeIds, null, null, null,
                null, null, null, null, null, null, null, null, null);
        template.deleteDocuments(query);
        return dao.addDocument(siteId, treeNumber, taskService, taskId);
    }

    private Analyzer analyzer;
    private LuceneIndexTemplate template;
    private InfoQueryService service;
    private InfoFulltextDao dao;

    @Autowired
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Autowired
    public void setTemplate(LuceneIndexTemplate template) {
        this.template = template;
    }

    @Autowired
    public void setInfoQueryService(InfoQueryService service) {
        this.service = service;
    }

    @Autowired
    public void setDao(InfoFulltextDao dao) {
        this.dao = dao;
    }
}

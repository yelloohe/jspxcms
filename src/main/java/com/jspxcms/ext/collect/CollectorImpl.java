package com.jspxcms.ext.collect;

import com.jspxcms.core.domain.Info;
import com.jspxcms.core.domain.InfoDetail;
import com.jspxcms.core.service.InfoService;
import com.jspxcms.ext.domain.Collect;
import com.jspxcms.ext.service.CollectService;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectorImpl implements Collector {
    private Logger logger = LoggerFactory.getLogger(CollectorImpl.class);

    public void start(Integer collectId) {
        new CollectThread(collectId).start();
    }

    public class CollectThread extends Thread {
        private Integer collectId;

        public CollectThread(Integer collectId) {
            this.collectId = collectId;
        }

        @Override
        public void run() {
            Collect collect = service.get(collectId);
            if (collect.isRunning()) {
                return;
            }
            service.running(collectId);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setUserAgent(collect.getUserAgent()).build();
            try {
                doCollect(httpclient, collect);
            } catch (Exception e) {
                logger.error("", e);
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error("close HttpClient error!", e);
            }
            service.ready(collectId);
        }

        private void doCollect(CloseableHttpClient httpclient, Collect collect)
                throws ClientProtocolException, IOException, URISyntaxException {
            List<URI> listUris = collect.getListUris();
            Integer nodeId = collect.getNode().getId();
            Integer siteId = collect.getSite().getId();
            Integer creatorId = collect.getUser().getId();
            String charset = collect.getCharset();
            String html;
            List<URI> itemUris;
            for (URI listUri : listUris) {
                if (!service.isRunning(collectId)) {
                    return;
                }
                html = Collect.fetchHtml(httpclient, listUri, charset);
                if (html == null) {
                    continue;
                }
                itemUris = collect.getItemUris(html, listUri);
                for (URI itemUri : itemUris) {
                    if (!service.isRunning(collectId)) {
                        return;
                    }
                    long millis = collect.getInterval();
                    if (millis > 0) {
                        try {
                            Thread.sleep(millis);
                        } catch (InterruptedException e) {
                            logger.error(null, e);
                        }
                    }
                    Info info = new Info();
                    InfoDetail detail = new InfoDetail();
                    Map<String, String> customs = new HashMap<String, String>();
                    Map<String, String> clobs = new HashMap<String, String>();
                    boolean success = service.collcetItem(httpclient, itemUri,
                            collectId, charset, nodeId, creatorId, info,
                            detail, customs, clobs);
                    String status = Info.COLLECTED;
                    if (collect.getSubmit()) {
                        status = Info.NORMAL;
                    }
                    if (success) {
                        infoService.save(info, detail, null, null, null, null,
                                customs, clobs, null, null, null, null, null,
                                nodeId, creatorId, status, siteId, null);
                    }
                }
            }
        }
    }

    private InfoService infoService;
    private CollectService service;

    @Autowired
    public void setInfoService(InfoService infoService) {
        this.infoService = infoService;
    }

    @Autowired
    public void setService(CollectService service) {
        this.service = service;
    }

}

package com.jspxcms.core.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jspxcms.common.orm.Limitable;
import com.jspxcms.common.orm.RowSide;
import com.jspxcms.common.orm.SearchFilter;
import com.jspxcms.common.util.JsonMapper;
import com.jspxcms.core.domain.Model;
import com.jspxcms.core.domain.ModelField;
import com.jspxcms.core.repository.ModelFieldDao;
import com.jspxcms.core.service.ModelFieldService;
import com.jspxcms.core.service.ModelService;

/**
 * ModelFieldServiceImpl
 * 
 * @author liufang
 * 
 */
@Service
@Transactional(readOnly = true)
public class ModelFieldServiceImpl implements ModelFieldService {
	public List<ModelField> findList(Integer modelId) {
		Sort sort = new Sort(Direction.ASC, "seq", "id");
		return dao.findAll(spec(modelId, null), sort);
	}

	public RowSide<ModelField> findSide(Integer modelId, ModelField bean,
			Integer position) {
		if (position == null) {
			return new RowSide<ModelField>();
		}
		Sort sort = new Sort(Direction.ASC, "seq", "id");
		Limitable limit = RowSide.limitable(position, sort);
		List<ModelField> list = dao.findAll(spec(modelId, null), limit);
		return RowSide.create(list, bean);
	}

	private Specification<ModelField> spec(final Integer modelId,
			Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		final Specification<ModelField> fsp = SearchFilter.spec(filters,
				ModelField.class);
		Specification<ModelField> sp = new Specification<ModelField>() {
			public Predicate toPredicate(Root<ModelField> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate pred = fsp.toPredicate(root, query, cb);
				if (modelId != null) {
					pred = cb.and(pred,
							cb.equal(root.get("model").get("id"), modelId));
				}
				return pred;
			}
		};
		return sp;
	}

	public ModelField get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public ModelField[] batchSave(Integer modelId, String[] name,
			String[] label, Boolean[] dblColumn, String[] property,
			String[] custom) {
		ModelField[] beans = new ModelField[name.length];
		JsonMapper mapper = new JsonMapper();
		Map<String, String> customs;
		for (int i = 0, len = name.length; i < len; i++) {
			beans[i] = new ModelField();
			mapper.update(property[i], beans[i]);
			beans[i].setName(name[i]);
			beans[i].setLabel(label[i]);
			beans[i].setDblColumn(dblColumn[i]);
			customs = new HashMap<String, String>();
			mapper.update(custom[i], customs);
			save(beans[i], modelId, customs, null);
		}
		return beans;
	}

	@Transactional
	public ModelField save(ModelField bean, Integer modelId,
			Map<String, String> customs, Boolean clob) {
		Model model = modelService.get(modelId);
		bean.setModel(model);
		bean.setCustoms(customs);
		if (clob != null && clob) {
			bean.setInnerType(ModelField.FIELD_CUSTOM_CLOB);
		}
		bean.applyDefaultValue();
		bean = dao.save(bean);
		model.addField(bean);
		return bean;
	}

	@Transactional
	public ModelField copy(ModelField bean, Model model) {
		ModelField target = new ModelField();
		BeanUtils.copyProperties(bean, target, new String[] { "id", "customs",
				"model" });
		target.setCustoms(new HashMap<String, String>(bean.getCustoms()));
		target.setModel(model);
		dao.save(target);
		return target;
	}

	@Transactional
	public ModelField clone(ModelField field, Model model) {
		ModelField dest = new ModelField();
		BeanUtils.copyProperties(field, dest);
		dest.setId(null);
		Map<String, String> mapDest = new HashMap<String, String>(
				field.getCustoms());
		save(dest, model.getId(), mapDest, null);
		return dest;
	}

	@Transactional
	public ModelField[] batchUpdate(Integer[] id, String[] name,
			String[] label, Boolean[] dblColumn) {
		ModelField[] beans = new ModelField[id.length];
		Model model = null;
		for (int i = 0, len = id.length; i < len; i++) {
			beans[i] = get(id[i]);
			if (model == null) {
				model = beans[i].getModel();
				// 清空模型字段，防止应二级缓存导致脏数据。
				model.getFields().clear();
			}
			model.addField(beans[i]);
			// 只有自定义字段才允许改名称，系统字段不允许修改名称。
			if (beans[i].getInnerType() == ModelField.FIELD_CUSTOM) {
				beans[i].setName(name[i]);
			}
			beans[i].setLabel(label[i]);
			beans[i].setSeq(i);
			beans[i].setDblColumn(dblColumn[i]);
			update(beans[i], null, null);
		}
		return beans;
	}

	@Transactional
	public ModelField update(ModelField bean, Map<String, String> customs,
			Boolean clob) {
		if (customs != null) {
			bean.getCustoms().clear();
			bean.getCustoms().putAll(customs);
		}
		// 只有自定义字段才可以设置是否为大字段
		if (bean.isCustom() && clob != null) {
			bean.setInnerType(clob ? ModelField.FIELD_CUSTOM_CLOB
					: ModelField.FIELD_CUSTOM);
		}
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public ModelField[] disable(Integer[] ids) {
		ModelField[] beans = new ModelField[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = dao.findOne(ids[i]);
			beans[i].setDisabled(true);
		}
		return beans;
	}

	@Transactional
	public ModelField[] enable(Integer[] ids) {
		ModelField[] beans = new ModelField[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = dao.findOne(ids[i]);
			beans[i].setDisabled(false);
		}
		return beans;
	}

	@Transactional
	public ModelField delete(Integer id) {
		ModelField entity = dao.findOne(id);
		entity.getModel().getFields().remove(entity);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public ModelField[] delete(Integer[] ids) {
		ModelField[] beans = new ModelField[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}

	private ModelService modelService;

	@Autowired
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	private ModelFieldDao dao;

	@Autowired
	public void setDao(ModelFieldDao dao) {
		this.dao = dao;
	}
}

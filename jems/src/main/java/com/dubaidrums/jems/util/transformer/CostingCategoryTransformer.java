package com.dubaidrums.jems.util.transformer;

import com.dubaidrums.jems.domain.JemsCostingCategory;

import flexjson.transformer.AbstractTransformer;

public class CostingCategoryTransformer extends AbstractTransformer {

    public void transform(Object object) {
        getContext().write(((JemsCostingCategory) object).getId().toString());
    }

}
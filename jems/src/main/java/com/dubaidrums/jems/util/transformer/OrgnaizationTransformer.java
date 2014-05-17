package com.dubaidrums.jems.util.transformer;

import com.dubaidrums.jems.domain.JemsOrganization;

import flexjson.transformer.AbstractTransformer;

public class OrgnaizationTransformer extends AbstractTransformer {

    public void transform(Object object) {
        getContext().write(((JemsOrganization) object).getId().toString());
    }

}
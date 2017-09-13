/**
 * Copyright (c) 2000-2017 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.test.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.Application;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;


/**
 * @author  Kyle Stiemann
 */
@Named
@FlowScoped(Flow2Bean.TITLE)
public class Flow2Bean implements Serializable {

	static final String TITLE = "flow2";
	private static final long serialVersionUID = -4380151863471254093L;

	@PreDestroy
	public void destroy() {
		System.out.println("Flow2Bean @PreDestroy Called.");
	}

	public String getTitle() {
		return TITLE;
	}

	@PostConstruct
	public void initialize() {
		System.out.println("Flow2Bean @PostConstruct Called.");
	}

	public String returnFromFlow2() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Application application = facesContext.getApplication();
		ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler)
			application.getNavigationHandler();
		configurableNavigationHandler.getNavigationCase(facesContext, "returnFromFlow2", "returnFromFlow2");

		return "returnFromFlow2";
	}
}

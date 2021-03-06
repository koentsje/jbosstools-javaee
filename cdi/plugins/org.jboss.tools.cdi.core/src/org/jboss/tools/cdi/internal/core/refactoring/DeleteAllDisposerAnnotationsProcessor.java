/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.internal.core.refactoring;

import java.util.Collection;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.text.edits.MultiTextEdit;
import org.jboss.tools.cdi.core.CDIConstants;
import org.jboss.tools.cdi.core.IBeanMethod;
import org.jboss.tools.cdi.core.IClassBean;
import org.jboss.tools.cdi.core.IProducer;
import org.jboss.tools.cdi.core.IProducerMethod;

public class DeleteAllDisposerAnnotationsProcessor extends CDIRefactoringProcessor {
	private IMethod method;
	
	public DeleteAllDisposerAnnotationsProcessor(IFile file, IMethod method, String label){
		super(file, label);
		this.method = method;
	}
	
	private void changeDisposers(IClassBean bean) throws JavaModelException {
		if (bean.getDisposers().isEmpty()) {
			return;
		}

		for (IProducer producer : bean.getProducers()) {
			if (producer instanceof IProducerMethod) {
				IProducerMethod producerMethod = (IProducerMethod) producer;
				Collection<IBeanMethod> disposerMethods = producer.getCDIProject().resolveDisposers(producerMethod);
				ICompilationUnit original = producerMethod.getMethod().getCompilationUnit();
				ICompilationUnit compilationUnit = original.getWorkingCopy(new NullProgressMonitor());
				for (IBeanMethod disposerMethod : disposerMethods) {
					if(!disposerMethod.getMethod().isSimilar(method)){
						CDIMarkerResolutionUtils.deleteAnnotation(CDIConstants.DISPOSES_ANNOTATION_TYPE_NAME, compilationUnit, disposerMethod.getMethod().getParameters()[0], (MultiTextEdit)change.getEdit());
					}
				}
				compilationUnit.discardWorkingCopy();
			}
		}
	}
	
	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws CoreException,
			OperationCanceledException {
		
		createRootChange();

		if(bean != null)
			changeDisposers(bean);
		
		return status;
	}

}

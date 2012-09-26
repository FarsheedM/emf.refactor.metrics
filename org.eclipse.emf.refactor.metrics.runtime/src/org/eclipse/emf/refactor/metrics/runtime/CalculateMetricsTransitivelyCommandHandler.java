package org.eclipse.emf.refactor.metrics.runtime;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.refactor.metrics.managers.ProjectManager;
import org.eclipse.emf.refactor.metrics.managers.SelectionManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.ui.PlatformUI;

public class CalculateMetricsTransitivelyCommandHandler implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) { }

	@Override
	public void dispose() { }

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Cursor oldCursor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getCursor();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(new Cursor(null,SWT.CURSOR_WAIT));
		ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
		List<EObject> context = SelectionManager.getESelection(selection);
		IProject project = ProjectManager.getActualProject(selection);
		if (project == null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(oldCursor);
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"EMF Metrics", "Can not calculate metrics: project is " + project);
			return null;
		}
		EMFMetrics.calculateAllConfiguredMetrics(project, context.get(0));
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setCursor(oldCursor);
		MessageDialog.openInformation(null, null, "calculate metrics transitively: to do ...");
		return null;
	}

	@Override
	public boolean isEnabled() { return true; }

	@Override
	public boolean isHandled() { return true; }

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) { }

}

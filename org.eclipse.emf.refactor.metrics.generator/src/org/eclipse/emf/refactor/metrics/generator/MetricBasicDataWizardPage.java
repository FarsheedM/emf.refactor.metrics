package org.eclipse.emf.refactor.metrics.generator;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class MetricBasicDataWizardPage extends WizardPage implements Listener{

	private static final String PAGE_NAME = "org.eclipse.emf.refactor.metrics.MetricBasicDataWizardPage";
	private static final String PAGE_TITLE = "Basic Metric Data";
	private static final String PAGE_DESCRIPTION = "Please specify basic metric data. \n"
												+ "(required fields are denoted by \"*\")";
	private Text nameTextField, idTextField, descriptionTextField;
	private Combo projectCombo, metamodelCombo, contextCombo;
	private boolean initialization = false;
	private String jar = "";
	
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);
		createTextFields(container);
		initContents();
		setControl(container);
		this.setPageComplete(false);
	}	

	@Override
	public boolean canFlipToNextPage() {
		if(! initialization) {
			return false;
		}
		return this.isPageComplete();
	}
	
	@Override
	public void handleEvent(Event event) {
		if (event.widget == projectCombo) {
			((NewMetricWizardJava)getWizard()).setTargetProject(projectCombo.getText());
		}
		if (event.widget == metamodelCombo) {
			String nsURI = metamodelCombo.getText();
			if (nsURI != null && ! nsURI.isEmpty()) {
				EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(nsURI);
				if (ePackage != null) {
					jar = ePackage.getClass().getPackage().getName();
					contextCombo.removeAll();
					Object[] eObjectNames = new Object[ePackage.eContents().size()];
					for (int i = 0; i < eObjectNames.length; i++) {
						EObject eObject = ePackage.eContents().get(i);
						if (eObject instanceof ENamedElement) {
							eObjectNames[i] = ((ENamedElement) ePackage.eContents().get(i)).getName();
						}
					}					
			        Arrays.sort(eObjectNames);
					for(Object object : eObjectNames){
						contextCombo.add((String)object);
					}
				}
			} else {
				jar = "";
				contextCombo.removeAll();
			}
		}
		if(!initialization){
			updatePageComplete();
			getWizard().getContainer().updateButtons();		
		}
	}
	
	public WizardPage getNextPage() {
		return null;
	}

	public MetricBasicDataWizardPage() {
		super(PAGE_NAME);
		setTitle(PAGE_TITLE);
		setDescription(PAGE_DESCRIPTION);
	}
	
	private void createTextFields(Composite container) {
		Label label;
		GridData gridData;
		Group group;
		GridLayout layout;
		// - Project -
		group = new Group(container, SWT.NONE);
		group.setText("Project Data");
	    layout = new GridLayout();
	    layout.numColumns = 2;
	    group.setLayout(layout);
	    gridData = new GridData(GridData.FILL_HORIZONTAL);
	    group.setLayoutData(gridData);
		// col:1
		label = new Label(group, SWT.NONE);
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		label.setText("Target project *:");
		// col:2
		projectCombo = new Combo(group, SWT.READ_ONLY);
		final GridData projectComboData = new GridData(GridData.FILL_HORIZONTAL);
		projectCombo.setLayoutData(projectComboData);
		//projectCombo.setText("Select Project");
		projectCombo.addListener(SWT.Selection, this);
		// - Metric Data -
		group = new Group(container, SWT.NONE);
		group.setText("Metric Data");
	    layout = new GridLayout();
	    layout.numColumns = 2;
	    group.setLayout(layout);
	    gridData = new GridData(GridData.FILL_HORIZONTAL);
	    group.setLayoutData(gridData);
		// - Name -
		// col:1
		label = new Label(group, SWT.NONE);
		label.setText("Name *:");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		// col:2
		nameTextField = new Text(group, SWT.BORDER);
		nameTextField.addListener(SWT.Modify, this);
		nameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// - Id -
		// col:1
		label = new Label(group, SWT.NONE);
		label.setText("ID *:");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		// col:2
		idTextField = new Text(group, SWT.BORDER);
		idTextField.addListener(SWT.Modify, this);
		idTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// - Description -
		// col:1
		label = new Label(group, SWT.NONE);
		label.setText("Description:");
		label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		// col:2
		descriptionTextField = new Text(group, SWT.BORDER);
		descriptionTextField.addListener(SWT.Modify, this);
		descriptionTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// - Source -
		group = new Group(container, SWT.NONE);
		group.setText("Meta Model and Context Data");
	    layout = new GridLayout();
	    layout.numColumns = 2;
	    group.setLayout(layout);
	    gridData = new GridData(GridData.FILL_HORIZONTAL);
	    group.setLayoutData(gridData);
	    // - Metamodel -
	 	// col:1
	 	label = new Label(group, SWT.NONE);
	 	label.setText("Metamodel *:");
	 	label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	 	// col:2
	 	metamodelCombo = new Combo(group, SWT.BORDER);
	 	metamodelCombo.addListener(SWT.Selection, this);
	    // - Context -
	 	// col:1
	 	label = new Label(group, SWT.NONE);
	 	label.setText("Context *:");
	 	label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	 	// col:2
	 	contextCombo = new Combo(group, SWT.BORDER);
	 	contextCombo.addListener(SWT.Selection, this);
	}
	
	private void initContents() {
		initialization = true;
		initProjectsAndMetamodels();
		initialization = false;
	}
	
	private void initProjectsAndMetamodels(){
		for (IProject project : ((NewMetricWizardJava)getWizard()).getProjects()) {
			projectCombo.add(project.getName());
		}
		Object [] metamodelObjects = 
				EPackage.Registry.INSTANCE.keySet().toArray(new Object[EPackage.Registry.INSTANCE.size()]);
        Arrays.sort(metamodelObjects);
		for(Object object : metamodelObjects){
			metamodelCombo.add(object.toString());
		}
	}

	/**
	 * Wird jedes mal ausgef�hr wenn sich der Inhalt eines Textfeldes im Wizard
	 * ver�ndert. �berpr�ft die inhalte der Textfelder und erzeugt
	 * entschprechende Meldungen im Wizardfenster.
	 */
	private void updatePageComplete() {
		if(nameTextField.getText().isEmpty()){
			this.setMessage("Metric name is not specified.", ERROR);
			this.setPageComplete(false);
		}else
		if(idTextField.getText().isEmpty()){
			this.setMessage("Metric ID is not specified.", ERROR);
			this.setPageComplete(false);
		}else
		if(metamodelCombo.getText().isEmpty()){
			this.setMessage("Metric metamodel is not specified.", ERROR);
			this.setPageComplete(false);
		}else
		if( contextCombo.getText().isEmpty()){
			this.setMessage("Metric context is not specified.", ERROR);
			this.setPageComplete(false);
		}else
		if(!checkProject()){
			this.setMessage("Target project for the Metric is not specified.", ERROR);
			this.setPageComplete(false);
		}else{
			this.setMessage("");
			this.setPageComplete(true);
			saveTextFieldValues();
		}
	}
	
	private boolean checkProject(){
		if(!projectCombo.getText().isEmpty())
			return true;
		return false;
	}
	
	private void saveTextFieldValues(){
		((NewMetricWizardJava)getWizard()).setName(this.nameTextField.getText());
		((NewMetricWizardJava)getWizard()).setId(this.idTextField.getText());
		((NewMetricWizardJava)getWizard()).setDescription(this.descriptionTextField.getText());
		((NewMetricWizardJava)getWizard()).setMetamodel(this.metamodelCombo.getText());
		((NewMetricWizardJava)getWizard()).setContext(this.contextCombo.getText());
		((NewMetricWizardJava)getWizard()).setJar(jar);
	}

}
	


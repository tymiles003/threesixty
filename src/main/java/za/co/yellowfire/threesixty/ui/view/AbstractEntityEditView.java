package za.co.yellowfire.threesixty.ui.view;

import org.springframework.data.domain.Persistable;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.question.Service;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.NotificationBuilder;
import za.co.yellowfire.threesixty.ui.view.RatingQuestionWindow.SaveEvent;
import za.co.yellowfire.threesixty.ui.view.RatingQuestionWindow.SaveListener;

@SuppressWarnings("serial")
public abstract class AbstractEntityEditView<T extends Persistable<String>> extends AbstractDashboardPanel /*, DashboardEditListener*/ implements SaveListener {
	private static final long serialVersionUID = 1L;
	          
    private Button saveButton = ButtonBuilder.SAVE(this::onSave);
	private Button resetButton = ButtonBuilder.RESET(this::onReset);
	private Button createButton = ButtonBuilder.NEW(this::onCreate);	
    private Button[] buttons = new Button[] {saveButton, resetButton, createButton};
   
    private final Service<T> service;
    private final AbstractEntityEditForm<T> form;
    
    public AbstractEntityEditView(Service<T> service, AbstractEntityEditForm<T> form) {
		super();
		this.service = service;
		this.form = form;
	}

	@Override
	protected Component buildContent() {
		
    	VerticalLayout root = new VerticalLayout();
    	root.setSpacing(true);
        root.setMargin(false);
        Responsive.makeResponsive(root);
        
        root.addComponent(form); 
		return root;
	}
    
    protected Component buildHeaderButtons() {
        return new HeaderButtons(buttons);
 	}
    
	@Override
    public void enter(final ViewChangeEvent event) {
		String[] parameters = event.getParameters().split("/");
		if (parameters.length > 0) {
			form.bind(findEntity(parameters[0]));
		}
			
		build();
    }
	
	@Override
	public void entitySaved(SaveEvent e) { }
	
	protected abstract T findEntity(final String id);
	
	protected void onSave(ClickEvent event) {
		try {
			//Validate the field group
	        form.commit();
	        //Persist the outcome
	        T result = getService().save(form.getValue(), getCurrentUser());
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", "Outcome " + result.getId() + " updated successfully.", 2000);
	        //DashboardEventBus.post(new ProfileUpdatedEvent());
		} catch (CommitException exception) {
            Notification.show("Error while updating user", Type.ERROR_MESSAGE);
        }
	}
	
	protected void add(ClickEvent event) {
		if (form.isModified()) {
			ConfirmDialog.show(
					UI.getCurrent(), 
					"Confirmation", 
					"Would you like to discard you changes?",
					"Yes",
					"No",
			        new ConfirmDialog.Listener() {
			            public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                    //Discard the field group
			                	form.discard();
			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
			                    //Set a new data source
			                	form.bindToEmpty();
			                }
			            }
			        });
		} else {
			form.discard();
			form.bindToEmpty();
		}
	}
	
	protected void delete(ClickEvent event) {
		try {
			ConfirmDialog.show(
					UI.getCurrent(), 
					"Confirmation", 
					"Are you sure you would like to delete this rating question?",
					"Yes",
					"No",
			        new ConfirmDialog.Listener() {
			            public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                	//Delete the entity
			                    getService().delete(form.getValue(), getCurrentUser());
			                    //Notify the user of the outcome
			                    NotificationBuilder.showNotification("Update", "Rating question updated successfully", 2000);
			                    //Discard the field group
			                    form.discard();
			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
			                }
			            }
			        });
		} catch (Exception e) {
            Notification.show("Error while deleting outcome", Type.ERROR_MESSAGE);
        }
	}
	
	protected void onReset(ClickEvent event) {
	}
	
	protected void onCreate(ClickEvent event) {
	}
	
	protected User getCurrentUser() {
		return ((MainUI) UI.getCurrent()).getCurrentUser();
	}
		
	protected Service<T> getService() { return this.service; }
}


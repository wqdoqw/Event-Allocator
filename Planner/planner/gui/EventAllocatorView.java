package planner.gui;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import planner.Event;
import planner.FormatException;
import planner.Venue;

/**
 * The view for the event allocator program.
 */
public class EventAllocatorView {

	// the model of the event allocator
	private EventAllocatorModel model;
	// the scene behind it
	private Scene scene;
	// instance variable for buttons of event for adding event button
	private Button addEventButton;
	// instance variable for text field for event name for event name
	private TextField eventName;
	// instance variable for size of the event for text field for event size
	private TextField eventSize;
	// instance variable for combo box of event type for event box
	private ComboBox<Event> eventBox;
	// instance variable for combo box of venue type for venue box
	private ComboBox<Venue> venueBox;
	// instance variable for button for allocation button
	private Button allocateButton;
	// instance variable for combo box of event type for event delete box
	private ComboBox<Event> eventDeleteBox;
	// instance variable for button for delete button for delete button
	private Button deleteButton;
	// instance variable of table view for Pair sub-class type for allocation
	// table
	private TableView<Pair> allocationTable;
	// instance variable of table view for Pair sub-class type for corridor
	// table
	private TableView<Pair> corridorTable;

	/**
	 * Initializes the view for the event allocator program.
	 *
	 * @param model
	 *            the model for the event allocator program.
	 */
	public EventAllocatorView(EventAllocatorModel model) {
		this.model = model;

		// creates a GridPane layout
		GridPane eventPane = new GridPane();
		eventPane.setPadding(new Insets(20, 20, 20, 20));
		eventPane.setVgap(5);
		eventPane.setHgap(5);

		// creates a label with "Add New Event"
		Label addEvent = new Label("Add New Event");
		addEvent.setFont(new Font("Arial", 16));
		GridPane.setConstraints(addEvent, 0, 0);
		eventPane.getChildren().add(addEvent);

		addEventButton = new Button("Add Event");
		GridPane.setConstraints(addEventButton, 0, 3);
		eventPane.getChildren().add(addEventButton);

		eventName = new TextField();
		eventName.setPromptText("Enter name of the event");
		GridPane.setConstraints(eventName, 0, 1);
		eventPane.getChildren().add(eventName);

		eventSize = new TextField();
		eventSize.setPromptText("Enter size of the event");
		GridPane.setConstraints(eventSize, 0, 2);
		eventPane.getChildren().add(eventSize);

		// creates a label with "Allocate Event"
		Label allocateEvent = new Label("Allocate Event");
		allocateEvent.setFont(new Font("Arial", 16));
		GridPane.setConstraints(allocateEvent, 1, 0);
		eventPane.getChildren().add(allocateEvent);

		eventBox = new ComboBox<>();
		eventBox.setPromptText("Select an event");
		GridPane.setConstraints(eventBox, 1, 1);
		eventPane.getChildren().add(eventBox);

		venueBox = new ComboBox<>();
		venueBox.setPromptText("Select a venue");
		GridPane.setConstraints(venueBox, 1, 2);
		eventPane.getChildren().add(venueBox);

		allocateButton = new Button("Allocate");
		GridPane.setConstraints(allocateButton, 1, 3);
		eventPane.getChildren().add(allocateButton);

		// creates a label with "Remove Event Allocation"
		Label removeEvent = new Label("Remove Event Allocation");
		removeEvent.setFont(new Font("Arial", 16));
		GridPane.setConstraints(removeEvent, 2, 0);
		eventPane.getChildren().add(removeEvent);

		eventDeleteBox = new ComboBox<>();
		eventDeleteBox.setPromptText("Select an event");
		GridPane.setConstraints(eventDeleteBox, 2, 1);
		eventPane.getChildren().add(eventDeleteBox);

		deleteButton = new Button("Remove Allocation");
		GridPane.setConstraints(deleteButton, 2, 2);
		eventPane.getChildren().add(deleteButton);

		// creating an instance of table column called "Events"
		TableColumn<Pair, String> eventCol = new TableColumn<>("Events");
		eventCol.setMinWidth(150);
		eventCol.setCellValueFactory(new PropertyValueFactory<>("event"));

		// creating an instance of table column called "Venues"
		TableColumn<Pair, String> venueCol = new TableColumn<>("Venues");
		venueCol.setMinWidth(150);
		venueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));

		allocationTable = new TableView<>();
		allocationTable.getColumns().add(eventCol);
		allocationTable.getColumns().add(venueCol);
		allocationTable.setMinHeight(400);
		GridPane.setConstraints(allocationTable, 0, 4);
		eventPane.getChildren().add(allocationTable);

		// creating a table column called "Corridors"
		TableColumn<Pair, String> corridorCol = new TableColumn<>("Corridors");
		corridorCol.setMinWidth(150);
		corridorCol.setCellValueFactory(new PropertyValueFactory<>("event"));

		// creating a table column called "Traffic"
		TableColumn<Pair, String> trafficCol = new TableColumn<>("Traffic");
		trafficCol.setMinWidth(100);
		trafficCol.setCellValueFactory(new PropertyValueFactory<>("venue"));

		corridorTable = new TableView<>();
		corridorTable.getColumns().add(corridorCol);
		corridorTable.getColumns().add(trafficCol);
		corridorTable.setMinHeight(300);
		GridPane.setConstraints(corridorTable, 2, 4);
		eventPane.getChildren().add(corridorTable);

		scene = new Scene(eventPane);
	}

	/**
	 * Returns the scene for the event allocator application.
	 *
	 * @return returns the scene for the application
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * This method is used to return model which is the event allocator program.
	 * 
	 * @return model which is the event allocator program.
	 */
	public EventAllocatorModel getModel() {
		return model;
	}

	/**
	 * This method is used to return event buttons for adding a button for
	 * events.
	 * 
	 * @return addEventButton which is used for adding a button for events.
	 */
	public Button getAddEventButton() {
		return addEventButton;
	}

	/**
	 * This method is used to get event names for a text field.
	 * 
	 * @return eventName which is used for a text field for event names.
	 */
	public TextField getEventName() {
		return eventName;
	}

	/**
	 * This method is used to get event sizes for a text field.
	 * 
	 * @return eventSize which is used for ta ext field for event sizes.
	 */
	public TextField getEventSize() {
		return eventSize;
	}

	/**
	 * This method is used to get a combo box of events.
	 * 
	 * @return eventBox which is used for a combo box for events.
	 */
	public ComboBox<Event> getEventBox() {
		return eventBox;
	}

	/**
	 * This method is used to get a combo box for venue.
	 * 
	 * @return venueBox which is used for a combo box for venue.
	 */
	public ComboBox<Venue> getVenueBox() {
		return venueBox;
	}

	/**
	 * This method is used to get a button for allocation.
	 * 
	 * @return allocateButton which is used for a button for allocation.
	 */
	public Button getAllocateButton() {
		return allocateButton;
	}

	/**
	 * This method is used to get combo box for event delete box.
	 * 
	 * @return eventDeleteBox which is used for a combo box for event delete
	 *         box.
	 */
	public ComboBox<Event> getEventDeleteBox() {
		return eventDeleteBox;
	}

	/**
	 * This method is used to get a button for delete button.
	 * 
	 * @return deleteButton which is used for a button for the delete button.
	 */
	public Button getDeleteButton() {
		return deleteButton;
	}

	/**
	 * This method is used to get a table for a allocation table.
	 * 
	 * @return allocationTable which is used for a table for a allocation table.
	 */
	public TableView<Pair> getAllocationTable() {
		return allocationTable;
	}

	/**
	 * This method is used to get a table for a corridor table.
	 * 
	 * @return corridorTable which is used for a corridor table.
	 */
	public TableView<Pair> getCorridorTable() {
		return corridorTable;
	}

	/**
	 * This method is used to show warning for IOException and FormatException.
	 * 
	 * @param exception
	 *            indicates conditions that the program might want to catch.
	 */
	public void showWarning(Exception exception) {
		// Creates an alert with warning type
		Alert alert = new Alert(AlertType.WARNING);
		if (exception instanceof IOException) {
			alert.setTitle("IOException!");
		} else if (exception instanceof FormatException) {
			alert.setTitle("FormatException!");
		}
		alert.setHeaderText(null);
		alert.setContentText(exception.getMessage());
		alert.show();
	}

	/**
	 * This method is used to show alerts with messages according to the errors.
	 * 
	 * @param message
	 *            appropriate messages according to the errors.
	 */
	public void showMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(message);
		alert.show();
	}

	/**
	 * This sub class is used to provide a full implementation Property wrapping
	 * a String value. And, this sub class is used when updating the data.
	 */
	public static class Pair {

		// creating an instance variable for event
		private final SimpleStringProperty event;
		// creating an instance variable for venue
		private final SimpleStringProperty venue;

		public Pair(String eventString, String venueString) {

			event = new SimpleStringProperty(eventString);
			venue = new SimpleStringProperty(venueString);
		}

		/**
		 * This method is used to return the string format of events.
		 * 
		 * @return event.get() which is string representation of events.
		 */
		public String getEvent() {
			return event.get();
		}

		/**
		 * This method is used to return the string format of venues.
		 * 
		 * @return venue.get() which is string representation of venues.
		 */
		public String getVenue() {
			return venue.get();
		}
	}
}

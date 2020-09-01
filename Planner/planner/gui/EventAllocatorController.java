package planner.gui;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import planner.Corridor;
import planner.Event;
import planner.FormatException;
import planner.InvalidTrafficException;
import planner.Traffic;
import planner.Venue;
import planner.VenueReader;
import planner.gui.EventAllocatorView.Pair;

/**
 * The controller for the event allocator program.
 */
public class EventAllocatorController {

	// the model of the event allocator
	private EventAllocatorModel model;
	// the view of the event allocator
	private EventAllocatorView view;
	// the list of listeners to track changes when they occur
	private final ObservableList<Pair> allocationData;
	// the list of listeners to track changes when they occur
	private final ObservableList<Pair> corridorData;

	/**
	 * Initializes the controller for the event allocator program. If file name
	 * is not valid, appropriate message should be shown to the users.
	 *
	 * @param model
	 *            the model of the event allocator
	 * @param view
	 *            the view of the event allocator
	 */
	public EventAllocatorController(EventAllocatorModel model,
			EventAllocatorView view) {
		this.model = model;
		this.view = view;
		this.allocationData = FXCollections.observableArrayList();
		this.corridorData = FXCollections.observableArrayList();

		try {
			// filename is to be specified here
			String fileName = "venues.txt";
			if (fileName.isEmpty()) {
				view.showMessage("Please give the filename of venues.");
				return;
			}
			model.setVenues(VenueReader.read(fileName));
			view.getVenueBox().getItems().setAll(model.getVenues());
		} catch (IOException | FormatException | InvalidTrafficException ex) {
			view.showWarning(ex);
		}
		view.getAllocateButton().setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * Handles a specific of event based on user inputs. If the users'
			 * input are not in correct format, this method shows warning
			 * messages.
			 * 
			 * @param event
			 *            the event which is to be handled.
			 */
			@Override
			public void handle(ActionEvent event) {
				// returns the currently selected object for events
				Event curEvent = (Event) view.getEventBox().getSelectionModel()
						.getSelectedItem();
				// returns the currently selected object for venues
				Venue curVenue = (Venue) view.getVenueBox().getSelectionModel()
						.getSelectedItem();
				if (curEvent == null || curVenue == null) {
					view.showMessage("Please select event and venue properly.");
					return;
				}
				if (model.getAllocationMap().containsKey(curEvent)) {
					view.showMessage("The specified event is currently\n"
							+ " allocated to a venue in the municipality");
					return;
				}
				if (model.getAllocationMap().containsValue(curVenue)) {
					view.showMessage("The selected venue is currently\n "
							+ "allocated to another event");
					return;
				}
				if (curVenue.canHost(curEvent)) {
					// getting the traffic from venue
					Traffic curTraffic = curVenue.getTraffic(curEvent);
					if (model.isSafe(curTraffic)) {
						model.getAllocationMap().put(curEvent, curVenue);
						model.updateCorridorMap(curTraffic);
						view.getEventDeleteBox().getItems()
								.setAll(model.getAllocationMap().keySet());
						writeData();
					} else {
						view.showMessage("The venue is large enough to "
								+ "host the event\n"
								+ "but allocating the given event to the"
								+ " selected venue\n"
								+ "would make the traffic caused by the current"
								+ " allocation unsafe");
					}
				} else {
					view.showMessage("The venue is not large\n enough "
							+ "to host the event");
				}
			}
		});

		view.getAddEventButton().setOnAction(new EventHandler<ActionEvent>() {

			/**
			 * Handles a specific of event based on user inputs. If the users'
			 * input are not in correct format, this method shows warning
			 * messages.
			 * 
			 * @param event
			 *            the event which is to be handled.
			 */
			@Override
			public void handle(ActionEvent event) {
				// getting the value of the text for name
				String name = view.getEventName().getText();
				// getting the size of the event for text
				String eventSize = view.getEventSize().getText();
				if (name.isEmpty()) {
					Exception ex = new NullPointerException(
							"The event name " + "cannot be null.");
					view.showWarning(ex);
					return;
				}
				if (eventSize.isEmpty()) {
					Exception ex = new NullPointerException(
							"The event size " + "cannot be null.");
					view.showWarning(ex);
					return;
				}
				// declaring event variable for adding events
				Event curEvent;
				try {
					// getting the size of the event in integer format 
					int size = Integer.parseInt(eventSize);
					curEvent = new Event(name, size);
					if (model.getEvents().contains(curEvent)) {
						view.showMessage("This event already exists.");
					} else {
						model.addEvents(curEvent);
					}
				} catch (NullPointerException | IllegalArgumentException ex) {
					view.showWarning(ex);
				}

				view.getEventBox().getItems().setAll(model.getEvents());
			}
		});

		view.getDeleteButton().setOnAction(new EventHandler<ActionEvent>() {
			/**
			 * Handles a specific of event based on user inputs. If the users'
			 * input are not in correct format, this method shows warning
			 * messages.
			 * 
			 * @param event
			 *            the event which is to be handled.
			 */
			@Override
			public void handle(ActionEvent event) {
				// getting event from EventAllocatorView class
				Event curEvent = (Event) view.getEventDeleteBox()
						.getSelectionModel().getSelectedItem();
				if (!model.getAllocationMap().containsKey(curEvent)) {
					view.showMessage("Please select event properly.");
					return;
				}
				// getting the venue from EventAllocatorModel class
				Venue curVenue = model.getAllocationMap().get(curEvent);
				// getting traffic form curVenue
				Traffic curTraffic = curVenue.getTraffic(curEvent);
				model.removeFromCorridorMap(curTraffic);
				model.getAllocationMap().remove(curEvent);
				view.getEventDeleteBox().getItems()
						.setAll(model.getAllocationMap().keySet());
				writeData();
			}
		});
	}

	/**
	 * Writes data which was handled by actionEvent handler. And, the format of
	 * corridor and event are changed to string format according to toString()
	 * representation. And, the newly handled data are displayed.
	 * 
	 */
	public void writeData() {
		allocationData.clear();
		corridorData.clear();
		// declaring variable for eventString and venueString
		String eventString, venueString;
		// creating an instance for TreeSet for looping
		TreeSet<Event> eventlist = new TreeSet<>(new EventComparator());
		eventlist.addAll(model.getAllocationMap().keySet());

		for (Event e : eventlist) {
			// getting venues from EventAllocatorModel class
			Venue venue = model.getAllocationMap().get(e);
			// getting toString representation of event
			eventString = e.toString();
			// getting right format for venue name and venue capacity
			venueString = venue.getName() + " (" + venue.getCapacity() + ")";
			// creating instance from pair sub class for table view
			Pair pair = new Pair(eventString, venueString);
			allocationData.add(pair);
		}
		// getting keyset for corridor and put them into corridorList
		Set<Corridor> corridorList = model.getCorridorMap().keySet();
		for (Corridor c : corridorList) {
			// getting amount of corridorMap
			Integer amount = model.getCorridorMap().get(c);
			if (amount > 0) {
				eventString = c.toString();
				venueString = amount.toString();
				// creating instance from Pair subclass for updating data
				Pair pair = new Pair(eventString, venueString);
				corridorData.add(pair);
			}
		}
		view.getAllocationTable().getItems().setAll(allocationData);
		view.getCorridorTable().getItems().setAll(corridorData);
	}

	/**
	 * This class sorts the events in alphabetical order of the name of the
	 * event. Events with equal names are ordered in ascending order of their
	 * sizes.
	 *
	 */
	class EventComparator implements Comparator<Event> {

		/**
		 * This method is used to compare two event names in alphabetical order
		 * of their event names and if the event names are equal, then, the 
		 * event names are ordered in ascending order of their sizes.
		 * 
		 * @param event1
		 *            event size to be compared with event2.
		 * @param event2
		 *            event size to be compared with event1.
		 */
		@Override
		public int compare(Event event1, Event event2) {
			if (event1.getName().equals(event2.getName())) {
				if (event1.getSize() > event2.getSize()) {
					return 1;
				} else if (event1.getSize() < event2.getSize()) {
					return -1;
				} else {
					return 0;
				}
			} else {
				return event1.getName().compareTo(event2.getName());
			}
		}
	}
}

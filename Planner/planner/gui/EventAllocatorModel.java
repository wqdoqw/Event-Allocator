package planner.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.TreeMap;

import planner.Corridor;
import planner.Event;
import planner.Traffic;
import planner.Venue;

/**
 * The model for the event allocator program.
 */
public class EventAllocatorModel {

	// array list of venue type
	private List<Venue> venues;
	// array list of event type
	private List<Event> events;
	// map of event and venue
	private Map<Event, Venue> allocationMap;
	// map of corridor and integer
	private Map<Corridor, Integer> corridorMap;

	/**
	 * the model for the event allocator program.
	 */
	public EventAllocatorModel() {
		venues = new ArrayList<>();
		events = new ArrayList<>();
		allocationMap = new HashMap<>();
		corridorMap = new HashMap<>();
	}

	/**
	 * Returns allocationMap which is the map representation of event and venue.
	 * 
	 * @return allocationMap which is the map representation of event and venue.
	 * 
	 */
	public Map<Event, Venue> getAllocationMap() {
		return allocationMap;
	}

	/**
	 * Returns corridorMap which is the map representation of corridor and
	 * integer.
	 * 
	 * @return corridorMap which is the map representation of corridor and
	 *         integer.
	 */
	public Map<Corridor, Integer> getCorridorMap() {
		return corridorMap;
	}

	/**
	 * This method is used to set allocationMap which is the map representation
	 * of event and venue.
	 * 
	 * @param allocationMap
	 *            which is used to set the allocationMap.
	 */
	public void setAllocationMap(Map<Event, Venue> allocationMap) {
		this.allocationMap = allocationMap;
	}

	/**
	 * This method is used to set corridorMap which is the map representation of
	 * corridor and integer.
	 * 
	 * @param corridorMap
	 *            which is used to set corridorMap.
	 */
	public void setCorridorMap(Map<Corridor, Integer> corridorMap) {
		this.corridorMap = corridorMap;
	}

	/**
	 * This method is used to set venues which is the array list of venue type.
	 * 
	 * @param venues
	 *            which is used to set venues.
	 */
	public void setVenues(List<Venue> venues) {
		this.venues = venues;
	}

	/**
	 * This method is used to set events which is array list of event type.
	 * 
	 * @param events
	 *            which is used to set events.
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}

	/**
	 * This method is used to get venues which is array list of venue type.
	 * 
	 * @return venues which is array list of venue type.
	 */
	public List<Venue> getVenues() {
		return venues;
	}

	/**
	 * This method is used to get events which is array list of event type.
	 * 
	 * @return events which is array list of event type.
	 */
	public List<Event> getEvents() {
		return events;
	}

	/**
	 * This method is used to add the event to the array list of event type.
	 * 
	 * @param event
	 *            which is used to add the event to the array list of event
	 *            type.
	 */
	public void addEvents(Event event) {
		events.add(event);
	}

	/**
	 * This method is used to update corridor map with traffic.
	 * 
	 * @param traffic
	 *            which is used to get corridor with traffic.
	 */
	public void updateCorridorMap(Traffic traffic) {
		// getting corridors with traffic
		Set<Corridor> corridorSet = traffic.getCorridorsWithTraffic();
		for (Corridor c : corridorSet) {
			// getting the number of traffic caused
			int value = traffic.getTraffic(c);
			if (corridorMap.containsKey(c)) {
				corridorMap.put(c, corridorMap.get(c) + value);
			} else {
				corridorMap.put(c, value);
			}
		}
	}

	/**
	 * This method is used to remove corridor map with traffic.
	 * 
	 * @param traffic
	 *            which is used to get corridor with traffic.
	 * 
	 */
	public void removeFromCorridorMap(Traffic traffic) {
		Set<Corridor> corridorSet = traffic.getCorridorsWithTraffic();
		for (Corridor c : corridorSet) {
			// getting the number of traffic caused
			int value = traffic.getTraffic(c);
			corridorMap.put(c, corridorMap.get(c) - value);
		}
	}

	/**
	 * This method is used to check for corridorMap is in correct format or not.
	 * 
	 * @param traffic
	 *            which is used to get corridor with traffic.
	 * @return true if the traffic is safe and false otherwise.
	 */
	public boolean isSafe(Traffic traffic) {
		// getting corridor with traffic
		Set<Corridor> corridorSet = traffic.getCorridorsWithTraffic();

		for (Corridor c : corridorSet) {
			// getting the number of traffic caused
			int value = traffic.getTraffic(c);
			if (corridorMap.containsKey(c)) {
				if (corridorMap.get(c) + value > c.getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}
}

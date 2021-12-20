package OOP2.Solution;


import OOP2.Provided.*;

import java.util.*;
import java.util.stream.Collectors;


public class PersonImpl implements Person {

	private Integer id;
	private String name;
	private List<Status> statuses;
	private Set<Person> friends;
	private Integer status_counter;

	/**
	 * Constructor receiving person's id and name.
	 */
	public PersonImpl(Integer id, String name)
	{
		this.id = id;
		this.name = name;
		this.statuses = new LinkedList<>();
		this.friends = new HashSet<>();
		this.status_counter = 0;
	}

	/**
	 * @return the person's id
	 */
	public Integer getId(){
		return id;
	}

	/**
	 * @return the person's name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Adds a new status to the person's collection of statuses.
	 *
	 * @param content - the status content
	 * @return the new status
	 */
	public Status postStatus(String content){

		Status newStatus = new StatusImpl(this,content,status_counter);

			statuses.add(newStatus); //add new status to the end of the list
			status_counter++;
			return newStatus;

	}

	/**
	 * Add a connection between this Person and another.
	 *
	 * @param p the Person to be added as a friend
	 *
	 * @throws SamePersonException if p is the same person as the current instance.
	 * @throws ConnectionAlreadyExistException if p is already a friend of this person
	 */
	public void addFriend(Person p) throws SamePersonException, ConnectionAlreadyExistException{
		if(this==p) throw new SamePersonException();
		if(friends.contains(p)) throw new ConnectionAlreadyExistException();
		friends.add(p);
	}

	/**
	 * @return collection of this person's friends
	 */
	public Collection<Person> getFriends(){
		return friends;
	}

	/**
	 * @return an iterable collection of all the person's statuses.
	 * 		The statuses are sorted by chronological descending order
	 * 		(LIFO order - last posted are first returned by iterator).
	 */
	public Iterable<Status> getStatusesRecent(){
		return statuses.stream()
				.sorted(new compareRec())
				.collect(Collectors.toList());
	}

	/**
	 * @return an iterable collection of all the person's statuses.
	 * 		The statuses are sorted by descending order of number of likes.
	 */
	public Iterable<Status> getStatusesPopular(){
		return statuses.stream()
				.sorted(new comparePop())
				.collect(Collectors.toList());
	}



	static class comparePop implements Comparator<Status>{
		@Override
		public int compare(Status s1, Status s2){
		return s2.getLikesCount()-s1.getLikesCount();
		}
	}

	static class compareRec implements Comparator<Status>{
		@Override
		public int compare(Status s1, Status s2){
			return s2.getId()-s1.getId();
		}
	}

	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null || getClass()!=o.getClass())
			return false;
		Person p = (Person)o;
		return id.equals(p.getId());
	}

	@Override
	public int compareTo(Person o){
		return this.id-o.getId();
	}

}

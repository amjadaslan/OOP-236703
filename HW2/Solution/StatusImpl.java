package OOP2.Solution;

import OOP2.Provided.Person;
import OOP2.Provided.Status;

import java.util.HashSet;
import java.util.Set;

public class StatusImpl implements Status {
	private Set<Integer> likes;
	private String content;
	private Integer id;
	private Person publisher;

	/*
	 * A constructor that receives the status publisher, the text of the status
	 *  and the id of the status.
	 */
	public StatusImpl(Person publisher, String content, Integer id)
	{
		this.publisher = publisher;
		this.id=id;
		this.content=content;
		this.likes = new HashSet<>();


	}



	public Integer getId(){
			return id;
		}

		/**
		 * @return the person who posted this status
		 */

	public Person getPublisher(){
		return publisher;
		}

		/**
		 * @return the text content of the status
		 */
		public String getContent(){
			return content;
		}

		/**
		 * @param p who is "liking" the status
		 */
		public void like(Person p){
				likes.add(p.getId());
		}

		/**
		 * @param p who is "unliking" the status
		 */
		public void unlike(Person p) {
				likes.remove(p.getId());
		}

		/**
		 * @return the number of people who currently like the status.
		 */
		public Integer getLikesCount(){
			return likes.size();
		}

	@Override
	public boolean equals(Object o){
		if(this == o) return true;
		if(o == null || getClass()!=o.getClass())
			return false;
		Status temp = (Status) o;
		return (id.equals(temp.getId()) && publisher.equals(temp.getPublisher()));
	}

}

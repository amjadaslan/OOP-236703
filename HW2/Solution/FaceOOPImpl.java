package OOP2.Solution;

import OOP2.Provided.*;

import java.util.*;
import java.util.stream.Collectors;

public class FaceOOPImpl implements FaceOOP {


    private LinkedList<Person> persons;


	/**
	 * Constructor - receives no parameters and initializes the system.
	 */
	public FaceOOPImpl()
	{
        persons = new LinkedList<>();
    }


    /**
     * Adds a new person to the system.
     *
     * @param id of person to join faceOOP
     * @param name of person to join faceOOP
     * @return the new instance of Person, created according to the received id and name.
     * @throws PersonAlreadyInSystemException if a person with the same id already exists in the system.
     */
    public Person joinFaceOOP(Integer id, String name) throws PersonAlreadyInSystemException{

        Person newPerson = new PersonImpl(id,name);
        if(persons.contains(newPerson)) throw new PersonAlreadyInSystemException();
        else persons.add(newPerson);
        return newPerson;
    }

    /**
     * @return the current number of users
     */
    public int size(){

        return persons.size();
    }

    /**
     * Using this method, you can get a Person for making changes on them
     * (i.e: post statuses)
     *
     * @param id of the person that is required.
     * @return the user with the requested id.
     * @throws PersonNotInSystemException if there is no such user in the system
     */
    public Person getUser(Integer id) throws PersonNotInSystemException{

            Iterator<Person> it = persons.iterator();

            while(it.hasNext()) {
                Person p = it.next();
                if(p.getId().equals(id)) return p;
            }
            throw new PersonNotInSystemException();
    }

    /**
     * Add a connection between 2 users.
     *
     * @param p1 the first person
     * @param p2 the second person
     * @throws PersonNotInSystemException if one of the
     * 		persons is not a user of the system.
     * @throws SamePersonException if p1 and p2 are the same person.
     * @throws ConnectionAlreadyExistException if the 2
     * 		users are already friends.
     */
    public void addFriendship(Person p1, Person p2)
            throws PersonNotInSystemException, SamePersonException, ConnectionAlreadyExistException{

        if(!persons.contains(p1) || !persons.contains(p2)) throw new PersonNotInSystemException();
        if(p1.equals(p2)) throw new SamePersonException();
        if(p1.getFriends().contains(p2) || p2.getFriends().contains(p1) )throw new ConnectionAlreadyExistException();

        getUser(p1.getId()).addFriend(p2);
        getUser(p2.getId()).addFriend(p1);

    }

    @Override
    public Iterator<Person> iterator() {
        return persons.iterator();
    }



    static class compareFrnds implements Comparator<Person>{
        @Override
        public int compare(Person p1, Person p2){
            return p1.getId()-p2.getId();
        }
    }

    /**
     * Returns an iterator implementing the StatusIterator interface.
     * The iterator returns the statuses of each of p's friends. Each
     * friend is visited by increasing order of id, and their statuses
     * are returned from the most recent to the oldest.
     *
     * @param p the person whose feed should be iterated over
     *
     * @return an iterator over the statuses in p's feed.
     * @throws PersonNotInSystemException in case p is not a user of the system.*/
    public StatusIterator getFeedByRecent(Person p)
            throws PersonNotInSystemException{
        if(!persons.contains(p)) throw new PersonNotInSystemException();

        List<Person> friendsOfP = p.getFriends().stream().sorted(new compareFrnds()).collect(Collectors.toList());

        StatusIteratorImpl si = new StatusIteratorImpl();


        for (Person person : friendsOfP) {

            Iterable<Status> statuses = person.getStatusesRecent();

            for( Status s : statuses){

                if(!si.res.contains(s)) si.res.add(s);

                }
        }


        return si;
    }


    /**
     * Returns an iterator implementing the StatusIterator interface.
     * The iterator returns the statuses of each of p's friends. Each
     * friend is visited by increasing order of id, and their statuses
     * are returned in order of most likes to fewest. If two statuses have
     * the same number of likes they should be ordered from newest to oldest.
     *
     * @param p the person whose feed should be iterated over
     *
     * @return an iterator over the statuses in p's feed.
     * @throws PersonNotInSystemException in case p is not a user of the system.*/
    public StatusIterator getFeedByPopular(Person p)
            throws PersonNotInSystemException{
        if(!persons.contains(p)) throw new PersonNotInSystemException();

        List<Person> friendsOfP = p.getFriends().stream().sorted(new compareFrnds()).collect(Collectors.toList());

        StatusIteratorImpl si = new StatusIteratorImpl();


        for (Person person : friendsOfP) {

            Iterable<Status> statuses = person.getStatusesPopular();

            for( Status s : statuses){

                if(!si.res.contains(s)) si.res.add(s);

            }


        }


        return si;
    }

    /**
     * Returns the length of the shortest path between the
     * source and the target (including target).
     * The path consists of friendships between users.
     *
     * @param source is the person that is the start point.
     * @param target is the person that is the end point.
     * @return the distance between source and target.
     * @throws PersonNotInSystemException if one of the Persons is not in the system,
     * and ConnectionDoesNotExistException if there is no path from source to target.
     */
    public Integer rank(Person source, Person target)
            throws PersonNotInSystemException, ConnectionDoesNotExistException{
        if(! ( this.persons.contains(source) && this.persons.contains(target) ) )
            throw new PersonNotInSystemException();

        int count = 1;

        LinkedList<Collection<Person>> temp = new LinkedList<>();
        LinkedList<Person> checked = new LinkedList<>();
        temp.add(source.getFriends());
        checked.add(source);

        while(!temp.isEmpty()){
            Collection<Person> col = temp.getFirst();
            for (Person person : col) {
                if(checked.contains(person)) continue;
                if(person.equals(target)) return count;
                checked.add(person);
                temp.add(person.getFriends());
            }
            count++;
            temp.remove(col);
            }


        throw new ConnectionDoesNotExistException();
    }

    static class StatusIteratorImpl implements StatusIterator{

        private List<Status> res;
        private Iterator<Status> it;

        public StatusIteratorImpl(){
            res = new LinkedList<>();
            it = null;
        }

        @Override
        public boolean hasNext(){
            if(it==null){
                it = res.iterator();
            }
            return it.hasNext();
        }

        @Override
        public Status next() {
            if(it==null){
                it = res.iterator();
            }
            return it.next();
        }

        @Override
        public void remove() {
            if(it==null){
                it = res.iterator();
            }
            it.remove();
        }
    }

}



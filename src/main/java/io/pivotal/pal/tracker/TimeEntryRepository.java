package io.pivotal.pal.tracker;
import java.util.List;

public interface TimeEntryRepository {
    public TimeEntry create (TimeEntry entry);
    public TimeEntry find (long id);
    public TimeEntry update (long id, TimeEntry entry);
    public List<TimeEntry> list();
    public void delete(long id);
}

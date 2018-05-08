package io.pivotal.pal.tracker;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long,TimeEntry> entries = new HashMap<>();

    public TimeEntry create (TimeEntry entry) {

        long id = entries.size() + 1L;

        TimeEntry newentry = new TimeEntry(id, entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());

        entries.put(id, newentry);
        return newentry;
    }

    public TimeEntry find (long id){
       return entries.get(id);
    }

    public TimeEntry update (long id, TimeEntry entry){

        TimeEntry newentry = new TimeEntry(id, entry.getProjectId(), entry.getUserId(), entry.getDate(), entry.getHours());
        entries.replace(id, newentry);
        return newentry;
    }

    @Override
    public List<TimeEntry> list() {


        return new ArrayList<>(entries.values());
    }

    @Override
    public void delete(long id) {
        entries.remove(id);
    }

}

package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TimeEntryController {
    private final CounterService counter;
    private final GaugeService gauge;

    private TimeEntryRepository timeEntriesRepo;


    public TimeEntryController(TimeEntryRepository timeEntriesRepo,CounterService counter,GaugeService gauge) {

        this.timeEntriesRepo = timeEntriesRepo;
        this.counter= counter;
        this.gauge = gauge;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry entry = timeEntriesRepo.create(timeEntryToCreate);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntriesRepo.list().size());
        return new ResponseEntity(entry,HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long id) {
        TimeEntry entry =  timeEntriesRepo.find(id);
        if(entry == null){
          return   ResponseEntity.notFound().build();
        }else {
            counter.increment("TimeEntry.read");
            return ResponseEntity.ok().body(entry);
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {

        counter.increment("TimeEntry.listed");
       return  ResponseEntity.ok().body(timeEntriesRepo.list());
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long id,@RequestBody TimeEntry entry) {
        TimeEntry updatedEntry = timeEntriesRepo.update(id,entry);
        if(updatedEntry == null){
            return ResponseEntity.notFound().build();
        }else {
            counter.increment("TimeEntry.updated");
            return ResponseEntity.ok().body(updatedEntry);
        }
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") long id) {
        timeEntriesRepo.delete(id);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntriesRepo.list().size());
        return ResponseEntity.noContent().build();
    }
}

package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TimeEntryController {


    private TimeEntryRepository timeEntriesRepo;


    public TimeEntryController(TimeEntryRepository timeEntriesRepo) {
        this.timeEntriesRepo = timeEntriesRepo;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry entry = timeEntriesRepo.create(timeEntryToCreate);

        return new ResponseEntity(entry,HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long id) {
        TimeEntry entry =  timeEntriesRepo.find(id);
        if(entry == null){
          return   ResponseEntity.notFound().build();
        }else

        return ResponseEntity.ok().body(entry);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {


       return  ResponseEntity.ok().body(timeEntriesRepo.list());
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long id,@RequestBody TimeEntry entry) {
        TimeEntry updatedEntry = timeEntriesRepo.update(id,entry);
        if(updatedEntry == null){
            return ResponseEntity.notFound().build();
        }else
        return ResponseEntity.ok().body(updatedEntry);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") long id) {
        timeEntriesRepo.delete(id);
        return ResponseEntity.noContent().build();
    }
}

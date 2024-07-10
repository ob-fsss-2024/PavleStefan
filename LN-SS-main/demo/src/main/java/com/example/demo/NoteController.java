package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping("/note/{title}")
    public List<NoteEntity> getNote(@PathVariable String title) {
        return noteRepository.findAllByTitle(title);
    }

    @PostMapping("/note")
    public NoteEntity createNote(@RequestBody CreateNote createNote) {
        NoteEntity note = new NoteEntity();
        note.setTitle(createNote.title());
        note.setText(createNote.text());

        return noteRepository.save(note);
    }
}
record CreateNote(String title, String text){

}


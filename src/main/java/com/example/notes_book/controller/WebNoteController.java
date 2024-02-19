package com.example.notes_book.controller;

import com.example.notes_book.model.Note;
import com.example.notes_book.repository.NoteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Позволил себе слегка отойти от главного запроса по заданию, сделал и веб версию и Rest API.
 *  Но, боюсь, мог напортачить где-то с главным запросом, просто мне интересно было визуализировать
 *  результат правильно. Еще, просьба к вам, напишите что не так с методом updateNote,
 *  внести изменения корректно не получается - "крашится" страница?!
 *  Хотелось еще в json сериаллизировать, но там беда вообще была, пришлось переделать на эту версию.
 *
 */
@Controller
@RequestMapping("/notes")
public class WebNoteController {

    private final NoteRepository noteRepository;

    public WebNoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    //представление главной страницы, доступна функция создания заметки
    @GetMapping("/view")
    public String viewNotes(Model model) {
        List<Note> notes = noteRepository.findAll();
        model.addAttribute("notes", notes);
        model.addAttribute("newNote", new Note());
        model.addAttribute("noteToUpdate", new Note());
        return "notes";
    }


    //создание заметки при наборе всех полей и нажатии кнопки
    @PostMapping("/add")
    public String addNote(@ModelAttribute("newNote") Note newNote) {
        newNote.setCreationDate(LocalDateTime.now());
        noteRepository.save(newNote);
        return "redirect:/notes/view";
    }

    //редактирование заметки, открывается модальное окно с редактором, пока не до конца правильно сделано.
    @PostMapping("/update/{id}")
    public String updateNote(@PathVariable Long id, @ModelAttribute("noteToUpdate") Note updatedNote) {
        Note existingNote = noteRepository.findById(id).orElse(null);

        if (existingNote != null) {
            existingNote.setTitle(updatedNote.getTitle());
            existingNote.setContent(updatedNote.getContent());
            noteRepository.save(existingNote);
        }

        return "redirect:/notes/view";
    }

    // при нажатии кнопки удаляет заметку, следует добавить и к этой кнопке модальное окно с выбором.
    @GetMapping("/{id}/delete")
    public String deleteNoteFromView(@PathVariable Long id) {
        noteRepository.deleteById(id);
        return "redirect:/notes/view";
    }
}

package com.example.lionminiboard.controller;

import com.example.lionminiboard.domain.Board;
import com.example.lionminiboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;


@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final PasswordEncoder passwordEncoder;




    @GetMapping("/list")
    public String boardList(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Board> boards = boardService.showAllList(pageable);

        model.addAttribute("boardList", boards.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boards.getTotalPages());
        model.addAttribute("size", size);
        return "board/list";
    }

   @GetMapping("/writeform")
    public String writeFormGet(Model model){
        model.addAttribute("board",new Board());
        return "board/writeform";
   }

   @PostMapping("/writeform")
    public String writeFormPost(@ModelAttribute Board board, RedirectAttributes redirectAttributes){
        boardService.saveBoard(board);
        redirectAttributes.addFlashAttribute("message","작성 완료.");
        return "redirect:/board/list";
   }
   //글 상세페이지
   @GetMapping("/view/{id}")
    public String DetailPage(@PathVariable Long id, Model model){
        model.addAttribute("board",boardService.findBoardById(id));
        return "board/view";
   }

   //글 삭제
    @GetMapping("/deleteform/{id}")
    public String deleteForm(@PathVariable Long id,Model model){
        model.addAttribute("board",boardService.findBoardById(id));
        return "board/deleteform";
    }

    @PostMapping("/deleteform/{id}")
    public String deleteBoardForm(@PathVariable Long id,@RequestParam String password,Model model){
        Board board = boardService.findBoardById(id);
        if (board != null && passwordEncoder.matches(password, board.getPassword())) {
            boardService.deleteById(id);
            return "redirect:/board/list";
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("board",board);
            return "board/deleteform";
        }

    }

    @GetMapping("/editform/{id}")
    public String editForm(@PathVariable Long id,Model model){
        model.addAttribute("board",boardService.findBoardById(id));
        return "board/editform";
    }

    @PostMapping("/editform")
    public String editFormPost(@RequestParam String password,@ModelAttribute Board board,RedirectAttributes redirectAttributes,Model model){
        Board existingBoard = boardService.findBoardById(board.getId());
        if (existingBoard != null && passwordEncoder.matches(password, existingBoard.getPassword())) {
            existingBoard.setName(board.getName());
            existingBoard.setTitle(board.getTitle());
            existingBoard.setContent(board.getContent());
            existingBoard.setUpdatedAt(LocalDateTime.now());
            boardService.saveBoard(existingBoard);
            return "redirect:/board/list";
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            model.addAttribute("board", board);
            return "board/editform";
        }
    }


}

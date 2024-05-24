package com.example.lionminiboard.service;

import com.example.lionminiboard.domain.Board;
import com.example.lionminiboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final PasswordEncoder passwordEncoder;
    public Page<Board> showAllList(Pageable pageable){
        Pageable sortedByDescId = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),Sort.by(Sort.Direction.DESC,"id"));
        return boardRepository.findAll(sortedByDescId);
    }

    @Transactional
    public void saveBoard(Board board){
        board.setCreatedAt(LocalDateTime.now());
        board.setPassword(passwordEncoder.encode(board.getPassword()));
        boardRepository.save(board);
    }

    public Board findBoardById(Long id){
        return boardRepository.findById(id).orElse(null);

    }
    @Transactional
    public void deleteById(Long id){
        boardRepository.deleteById(id);

    }
}

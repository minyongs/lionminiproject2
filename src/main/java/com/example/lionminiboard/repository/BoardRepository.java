package com.example.lionminiboard.repository;

import com.example.lionminiboard.domain.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends CrudRepository<Board,Long>, PagingAndSortingRepository<Board,Long> {
}

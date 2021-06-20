package com.kye.iStudyManageApp.sample.repository;

import com.kye.iStudyManageApp.sample.model.Board;
import com.kye.iStudyManageApp.sample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// JpaRepository는 자동으로 bean 등록이 된다. 따라서 @Repository를 붙이지 않아도 된다.
public interface BoardRepository extends JpaRepository<Board, Integer> {


}

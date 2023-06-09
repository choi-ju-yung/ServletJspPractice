package com.web.board.model.service;

import static com.web.common.JDBCTemplate.close;
import static com.web.common.JDBCTemplate.commit;
import static com.web.common.JDBCTemplate.getConnection;
import static com.web.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.util.List;

import com.web.board.model.dao.BoardDao;
import com.web.board.model.vo.Board;
import com.web.board.model.vo.BoardComment;

public class BoardService {
	private BoardDao dao=new BoardDao();
	
	public List<Board> selectBoard(int cPage, int numPerPage){
		Connection conn=getConnection();
		List<Board> boards=dao.selectBoard(conn,cPage,numPerPage);
		close(conn);
		return boards;
	}
	
	public int selectBoardCount() {
		Connection conn=getConnection();
		int result=dao.selectBoardCount(conn);
		close(conn);
		return result;
	}
	
	public Board selectBoardByNo(int no,boolean isRead) {
		Connection conn=getConnection();
		Board b=dao.selectBoardByNo(conn,no);
		if(b!=null && !isRead) { // 조회했을때 그 게시물이 null값이 아닐 경우
			int result = dao.updateBoardReadCount(conn,no);
			if(result>0) {
				commit(conn);
				b.setBoardReadCount(b.getBoardReadCount()+1); // 상세보기들어갔을때 바로 조회수가 눈에 증가된것을 보는화면
				// 이경우 중간에 다른사람이 들어올경우 수가 안맞을수도있음
			}
			else rollback(conn);
		}
		close(conn);
		return b;
	}
	
	public int insertBoard(Board b) {
		Connection conn=getConnection();
		int result=dao.insertBoard(conn,b);
		if(result>0)commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}
	
	public int updateBoard(Board b) {
		return 0;
	}
	
	public int deleteBoard(int no) {
		return 0;
	}
	
	
	public int insertBoardComment(BoardComment bc) {
		Connection conn=getConnection();
		int result=dao.insertBoardComment(conn,bc);
		if(result>0)commit(conn);
		else rollback(conn);
		close(conn);
		return result;
	}
	
	public List<BoardComment> selectBoardComment(int boardNo){
		Connection conn = getConnection();
		List<BoardComment> list=dao.selectBoardComment(conn,boardNo);
		close(conn);
		return list;
	}
}

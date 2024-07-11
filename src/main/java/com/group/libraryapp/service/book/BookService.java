package com.group.libraryapp.service.book;

import com.group.libraryapp.controller.book.BookRepository;
import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRespository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserLoanHistoryRespository userLoanHistoryRespository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserLoanHistoryRespository userLoanHistoryRespository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userLoanHistoryRespository = userLoanHistoryRespository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveBook(BookCreateRequest request){

        bookRepository.save(new Book(request.getName()));


    }

    @Transactional
    public void loanBook(BookLoanRequest request){
        //1. 책 정보 가져오기
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);

        //2. 대출 기록 정보를 확인해서 대출중인지 확인한다
        //3. 대출중이라면 예외를 발생시킨다.
        if(userLoanHistoryRespository.existsByBookNameAndIsReturn(book.getName(), false)){
            throw  new IllegalArgumentException("이미 대출 되었음");
        }

        // 4. 유저 정보를 가져온다.
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        user.loanBook(book.getName());
        // 5. 유저정보와 책 정보를 기반으로 UserLoanHistory를 저장

//        userLoanHistoryRespository.save(new UserLoanHistory(user, book.getName()));

    }

    @Transactional
    public void returnBook(BookReturnRequest request){
        // 1. 유저 이름으로 유저 ID 가져오기
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);
//        // 2. 유저 아이디와 책 이름으로 대출 기록 찾기
//        UserLoanHistory history = userLoanHistoryRespository.findByUserIdAndBookName(user.getId(), request.getBookName())
//                .orElseThrow(IllegalArgumentException::new);
//        // 3. 대출 기록을 반납 처리
//        history.doReturn();
////        userLoanHistoryRespository.save(history); // 트랜젝션의 변경감지 기능으로 자동 처리 됨.

        user.returnBook(request.getBookName());
    }
}

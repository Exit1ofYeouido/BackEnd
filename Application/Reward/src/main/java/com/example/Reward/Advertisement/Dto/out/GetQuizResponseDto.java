package com.example.Reward.Advertisement.Dto.out;

import com.example.Reward.Advertisement.Entity.Quiz;
import lombok.Builder;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GetQuizResponseDto {


    private String Question;

    private List<String> quizSectionList;

    private int answer;

    public static GetQuizResponseDto of(Quiz quiz) {

        return GetQuizResponseDto.builder()
                .Question(quiz.getQuestion())
                .quizSectionList(makeQuiz(quiz.getChoice1(),
                        quiz.getChoice2(),
                        quiz.getChoice3(),
                        quiz.getChoice4())).answer(quiz.getAnswer()).build();
    }

    private static List<String> makeQuiz(String choice1,String choice2,String choice3,String choice4){

        List<String> quizSections=new ArrayList<>();
        quizSections.add(choice1);
        quizSections.add(choice2);
        quizSections.add(choice3);
        quizSections.add(choice4);

        return quizSections;

    }
}

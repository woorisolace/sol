package com.example.clean.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "qna")
@SequenceGenerator(
    name = "qna_SEQ",
    sequenceName = "qna_SEQ",
    initialValue = 1,
    allocationSize = 1
)

public class QnAEntity extends BaseEntity {

  //기본키
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qna_SEQ")
  private Integer qnaId;

}

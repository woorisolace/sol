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
@Table(name = "review")
@SequenceGenerator(
    name = "review_SEQ",
    sequenceName = "review_SEQ",
    initialValue = 1,
    allocationSize = 1
)

public class ReviewEntity extends BaseEntity {

  //기본키
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_SEQ")
  private Integer reviewId;

}

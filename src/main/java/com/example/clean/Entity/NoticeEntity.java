package com.example.clean.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notice")
@SequenceGenerator(
    name = "notice_SEQ",
    sequenceName = "notice_SEQ",
    initialValue = 1,
    allocationSize = 1
)

public class NoticeEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notice_SEQ")
  private Integer noticeid;

}
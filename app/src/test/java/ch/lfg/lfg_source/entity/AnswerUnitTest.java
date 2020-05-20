package ch.lfg.lfg_source.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AnswerUnitTest {

  AnswerEntity answerEntity = new AnswerEntity(12, 13, true);

  @Test
  public void createAnswerTest() {
    AnswerEntity answerEntity = new AnswerEntity(12, 13, true);
    assertEquals(true, answerEntity.getAnswer());
    assertEquals(12, answerEntity.getGroupId());
    assertEquals(13, answerEntity.getUserId());
  }
}

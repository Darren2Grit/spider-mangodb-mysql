package com.exam.model.paper;

import java.util.ArrayList;
import java.util.List;

public class QuestionSingleChoice extends Question
{
  private List<Option> options;

  public QuestionSingleChoice(QuestionSingleChoice question)
  {
    setId(question.getId());
    setType(question.getType());
    setContent(question.getContent());
    setKey(question.getKey());
    setScore(question.getScore());
    setExt(question.getExt());

    setOptions(question.getOptions());
  }

  public QuestionSingleChoice() {
    setType("1");
  }

  public List<Option> getOptions() {
    return this.options;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }

  public void addOption(Option option) {
    if (this.options == null) {
      this.options = new ArrayList();
    }
    this.options.add(option);
  }
}
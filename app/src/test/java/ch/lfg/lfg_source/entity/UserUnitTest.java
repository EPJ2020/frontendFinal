package ch.lfg.lfg_source.entity;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserUnitTest {

  User user = new User();

  @Test
  public void setAndGetId() {
    user.setId(2);
    int id = user.getId();
    assertEquals(2, id);
  }

  @Test
  public void setAndGetName() {
    user.setLastName("Hungering");
    assertEquals("Hungering", user.getLastName());
  }

  @Test
  public void setAndGetFirstName() {
    user.setFirstName("Adam");
    assertEquals("Adam", user.getFirstName());
  }

  @Test
  public void setAndGetActive() {
    user.setActive(true);
    assertEquals(true, user.getActive());
  }

  @Test
  public void setAndGetDescription() {
    user.setDescription("Gross, rund und schlau");
    assertEquals("Gross, rund und schlau", user.getDescription());
  }

  @Test
  public void setAndGetTags() {
    ArrayList<String> tags = new ArrayList<String>();
    tags.add("C++");
    tags.add("Gross");
    tags.add("Stark");

    user.setTags(tags);
    assertEquals(tags, user.getTags());
  }

  @Test
  public void ToString() {
    user.setFirstName("Search");
    assertEquals(user.getFirstName(), user.toString());
  }
}

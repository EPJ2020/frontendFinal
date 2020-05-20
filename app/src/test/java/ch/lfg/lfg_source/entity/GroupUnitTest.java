package ch.lfg.lfg_source.entity;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroupUnitTest {

  Group group = new Group();

  @Test
  public void setAndGetId() {
    group.setOwnerId(2);
    int id = group.getOwnerId();
    assertEquals(2, id);
  }

  @Test
  public void setAndGetName() {
    group.setName("Hungering");
    assertEquals("Hungering", group.getName());
  }

  @Test
  public void setAndGetActive() {
    group.setActive(true);
    assertTrue(group.getActive());
  }

  @Test
  public void setAndGetDescription() {
    group.setDescription("Gross, rund und schlau");
    assertEquals("Gross, rund und schlau", group.getDescription());
  }

  @Test
  public void setAndGetPhoneNumber() {
    group.setPhoneNumber("4567892315");
    assertEquals("4567892315", group.getPhoneNumber());
  }

  @Test
  public void setAndGetEmail() {
    group.setEmail("abc@hsr.ch");
    assertEquals("abc@hsr.ch", group.getEmail());
  }

  @Test
  public void setAndGetTags() {
    ArrayList<String> tags = new ArrayList<String>();
    tags.add("C++");
    tags.add("Gross");
    tags.add("Stark");
    group.setTags(tags);
    assertEquals(tags, group.getTags());
  }

  @Test
  public void ToString() {
    group.setName("Search");
    assertEquals(group.getName(), group.toString());
  }
}

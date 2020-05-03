package com.example.lfg_source.entity;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
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
  public void changeAttributes() {
    ArrayList<String> tags = new ArrayList<String>();
    tags.add("C++");
    tags.add("Gross");
    tags.add("Stark");
    group.changeAttributes("lala", true, "MyGroup", "0456576555", "hey@hsr.ch", tags);
    assertEquals(tags, group.getTags());
    assertEquals("lala", group.getDescription());
    assertTrue(group.getActive());
    assertEquals("MyGroup", group.getName());
  }
}

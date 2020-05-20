package ch.lfg.lfg_source.login;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class LoginUnitTest {
  @Mock private LoginFacade loginFacade;
  @Mock LifecycleOwner lifecycleOwner;
  Lifecycle lifecycle;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    lifecycle = new LifecycleRegistry(lifecycleOwner);
    loginFacade = new LoginFacade(null);
  }

  @Test
  public void initialValueTest() {
    assertNull(loginFacade.getLoginFormState().getValue());
  }

  @Test
  public void UserIsValidTest() {
    assertTrue(loginFacade.isUserNameValid("Peter"));
  }

  @Test
  public void UserIsValid2Test() {
    assertTrue(loginFacade.isUserNameValid("bb@home.ch"));
  }

  @Test
  public void UserIsNotValidTest() {
    assertFalse(loginFacade.isUserNameValid(null));
  }

  @Test
  public void UserIsNotValid2Test() {
    assertFalse(loginFacade.isUserNameValid(""));
  }

  public void UserIsNotValid3Test() {
    assertFalse(loginFacade.isUserNameValid("      "));
  }

  @Test
  public void PasswordIsValidTest() {
    assertTrue(loginFacade.isPasswordValid("Peter123"));
  }

  @Test
  public void PasswordIsNotValidTest() {
    assertFalse(loginFacade.isPasswordValid(null));
  }

  @Test
  public void PasswordIsNotValid2Test() {
    assertFalse(loginFacade.isPasswordValid(""));
  }

  @Test
  public void PasswordIsNotValid3Test() {
    assertFalse(loginFacade.isPasswordValid("      "));
  }

  @Test
  public void PasswordIsNotValid4Test() {
    assertFalse(loginFacade.isPasswordValid("aa"));
  }
}

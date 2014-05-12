package com.acme.samples;

import java.util.LinkedHashMap;
import java.util.Map;

import org.exoplatform.social.webui.composer.UIActivityComposer;
import org.exoplatform.social.webui.composer.UIComposer.PostContext;
import org.exoplatform.web.application.ApplicationMessage;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.UIApplication;
import org.exoplatform.webui.core.UIComponent;
import org.exoplatform.webui.event.Event;
import org.exoplatform.webui.event.EventListener;
import org.exoplatform.webui.form.UIFormStringInput;

@ComponentConfig(template = "classpath:groovy/com/acme/samples/SampleActivityComposer.gtmpl", events = {
    @EventConfig(listeners = SampleActivityComposer.CheckinActionListener.class),
    @EventConfig(listeners = UIActivityComposer.CloseActionListener.class),
    @EventConfig(listeners = UIActivityComposer.SubmitContentActionListener.class),
    @EventConfig(listeners = UIActivityComposer.ActivateActionListener.class) })
public class SampleActivityComposer extends UIActivityComposer {

  public static final String LOCATION = "location";

  private String location_;

  private boolean isLocationValid_ = false;

  private Map<String, String> templateParams;

  public SampleActivityComposer() {
    System.out.println(SampleActivityComposer.class + ": " + "constructor called!");
    // WebuiRequestContext requestContext =
    // WebuiRequestContext.getCurrentInstance();
    // ResourceBundle resourceBundle =
    // requestContext.getApplicationResourceBundle();
    System.out.println("isReady= " + isReadyForPostingActivity());
    System.out.println("isDisplayed= " + isDisplayed());
    setReadyForPostingActivity(false);
    UIFormStringInput inputLocation = new UIFormStringInput("InputLocation", "InputLocation", null);
    addChild(inputLocation);
    setReadyForPostingActivity(true);
    setDisplayed(true);
    System.out.println("isReady= " + isReadyForPostingActivity());
    System.out.println("isDisplayed= " + isDisplayed());
  }

  public void setLocationValid(boolean isValid) {
    isLocationValid_ = isValid;
  }

  public boolean isLocationValid() {
    return isLocationValid_;
  }

  public void setTemplateParams(Map<String, String> tempParams) {
    templateParams = tempParams;
  }

  public Map<String, String> getTemplateParams() {
    return templateParams;
  }

  public void clearLocation() {
    location_ = null;
  }

  public String getLocation() {
    return location_;
  }

  private void setLocation(String city, WebuiRequestContext requestContext) {
    location_ = city;
    if (location_ == null || location_ == "") {
      UIApplication uiApp = requestContext.getUIApplication();
      uiApp.addMessage(new ApplicationMessage("abc", null, ApplicationMessage.WARNING));
      return;
    }

    templateParams = new LinkedHashMap<String, String>();
    templateParams.put(LOCATION, location_);

    setLocationValid(true);
  }

  @Override
  public void onActivate(Event<UIActivityComposer> uiActivityComposer) {
  }

  @Override
  public void onSubmit(Event<UIActivityComposer> uiActivityComposer) {
  }

  @Override
  public void onClose(Event<UIActivityComposer> uiActivityComposer) {
  }

  @Override
  public void onPostActivity(PostContext postContext,
                             UIComponent uiComponent,
                             WebuiRequestContext webuiRequestContext,
                             String str) {
  }

  public static class CheckinActionListener extends EventListener<SampleActivityComposer> {
    @Override
    public void execute(Event<SampleActivityComposer> event) throws Exception {

      System.out.println("Checkin button was clicked!");
      WebuiRequestContext requestContext = event.getRequestContext();
      SampleActivityComposer sampleActivityComposer = event.getSource();

      String city;
      try {
        city = requestContext.getRequestParameter(OBJECTID).trim();
      } catch (Exception e) {
        System.out.println(e.getMessage());
        return;
      }

      System.out.println("City= " + city);

      sampleActivityComposer.setLocation(city, requestContext);
      System.out.println(CheckinActionListener.class + ": " + sampleActivityComposer.location_);
      if (sampleActivityComposer.location_.length() > 0) {
        requestContext.addUIComponentToUpdateByAjax(sampleActivityComposer);
        event.getSource().setReadyForPostingActivity(true);
      }
    }
  }

}

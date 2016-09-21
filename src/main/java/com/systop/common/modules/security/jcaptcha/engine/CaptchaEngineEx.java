package com.systop.common.modules.security.jcaptcha.engine;

import java.awt.Color;
import java.awt.Font;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.GradientBackgroundGenerator;
import com.octo.captcha.component.image.color.SingleColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.LineTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.systop.common.modules.security.jcaptcha.JCaptchaConstants;

/**
 * Captcha增强版本
 * 
 * @author david.turing@gmail.com
 * @modifyTime 21:01:52
 * @description 
 * <pre>
 *  安装 Captcha Instruction <br>
 *  1.add captchaValidationProcessingFilter 
 *    to applicationContext-acegi-security.xml<br>
 *  2.modify applicationContext-captcha-security.xml
 *    <ul>
 *    <li> make sure that captchaValidationProcessingFilter Call captchaService
      <li> config CaptchaEngine for captchaService (refer imageCaptchaService) 
      <li> write your own CaptchaEngine
      <li> config the following, so that We use CaptchaEngineEx to generate the 
          captcha image. 
      </ul>
          &lt;constructor-arg
 *              type="com.octo.captcha.engine.CaptchaEngine" index="1"&gt; 
 *              &lt;ref bean="captchaEngineEx"/gt; &lt;/constructor-arg&gt; 
 * </pre>
 */
public class CaptchaEngineEx extends ListImageCaptchaEngine {
  /**
   * ...
   */
  protected void buildInitialFactories() {

     //Set Captcha Word Length Limitation which should not over 6
    Integer minAcceptedWordLength = new Integer(4);
    Integer maxAcceptedWordLength = new Integer(4);
    //Set up Captcha Image Size: Height and Width
    Integer imageHeight = new Integer(35);
    Integer imageWidth = new Integer(90);

    //Set Captcha Font Size
    Integer minFontSize = new Integer(16);
    Integer maxFontSize = new Integer(16);
    //We just generate digit for captcha source char Although you can use
    //abcdefg......xyz
    WordGenerator wordGenerator
      = new RandomWordGenerator(
        JCaptchaConstants.CAPTCHA_STRING);

     //cyt and unruledboy proved that backgroup not a factor of Security. A
     //captcha attacker won't affaid colorful backgroud, so we just use white
     //color, like google and hotmail.

    BackgroundGenerator backgroundGenerator = new GradientBackgroundGenerator(
        imageWidth, imageHeight, new Color(255, 255, 255), new Color(255, 255, 255));

     //font is not helpful for security but it really increase difficultness for
     //attacker
    FontGenerator fontGenerator = new RandomFontGenerator(minFontSize,
        maxFontSize, new Font[]{new Font("tahoma", Font.PLAIN, 16)});
     // Note that our captcha color is Blue
    SingleColorGenerator scg = new SingleColorGenerator(Color.blue);

     //decorator is very useful pretend captcha attack. we use two line text
     //decorators.

    LineTextDecorator lineDecorator = new LineTextDecorator(1, Color.blue);
    // LineTextDecorator line_decorator2 = new LineTextDecorator(1, Color.blue);
    TextDecorator[] textdecorators = new TextDecorator[1];

    textdecorators[0] = lineDecorator;
    // textdecorators[1] = line_decorator2;

    TextPaster textPaster = new DecoratedRandomTextPaster(
        minAcceptedWordLength, maxAcceptedWordLength, scg,
        new TextDecorator[] { });

    //ok, generate the WordToImage Object for logon service to use.
    WordToImage wordToImage = new ComposedWordToImage(
        fontGenerator, backgroundGenerator, textPaster);
    addFactory(new GimpyFactory(wordGenerator, wordToImage));
  }

}

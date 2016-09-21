package com.systop.common.modules.security.jcaptcha.engine;

import java.awt.Color;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.TwistedAndShearedRandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.systop.common.modules.security.jcaptcha.JCaptchaConstants;

/**
 * SpringSide Custom的认证图片
 * 
 * @author cac
 */
public class CaptchaEngine extends ListImageCaptchaEngine {
  /**
   * @see ListImageCaptchaEngine
   */
  protected void buildInitialFactories() {
    WordGenerator wordGenerator 
      = new RandomWordGenerator(JCaptchaConstants.CAPTCHA_STRING);
    // nteger minAcceptedWordLength, Integer maxAcceptedWordLength,Color[]
    // textColors
    TextPaster textPaster = new RandomTextPaster(JCaptchaConstants.CAPTCHA_MIN_WORDS,
        JCaptchaConstants.CAPTCHA_MAX_WORDS, Color.WHITE);
    // Integer width, Integer height
    BackgroundGenerator backgroundGenerator 
      = new FunkyBackgroundGenerator(JCaptchaConstants.CAPTCHA_IMG_WIDTH,
          JCaptchaConstants.CAPTCHA_IMG_HEIGHT);
    // Integer minFontSize, Integer maxFontSize
    FontGenerator fontGenerator = new TwistedAndShearedRandomFontGenerator(
        JCaptchaConstants.CAPTCHA_MIN_FONT_SIZE,
        JCaptchaConstants.CAPTCHA_MAX_FONT_SIZE);
    WordToImage wordToImage = new ComposedWordToImage(fontGenerator,
        backgroundGenerator, textPaster);
    addFactory(new GimpyFactory(wordGenerator, wordToImage));
  }
}

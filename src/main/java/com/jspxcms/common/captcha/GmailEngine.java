package com.jspxcms.common.captcha;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import com.jhlabs.image.PinchFilter;
import com.jhlabs.math.ImageFunction2D;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformation;
import com.octo.captcha.component.image.deformation.ImageDeformationByBufferedImageOp;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.GlyphsPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.GlyphsVisitors;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.OverlapGlyphsUsingShapeVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.TranslateAllToRandomPointVisitor;
import com.octo.captcha.component.image.textpaster.glyphsvisitor.TranslateGlyphsVerticalRandomVisitor;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;

/**
 * 仿Gmail验证码引擎
 * 
 * @author liufang
 * 
 */
public class GmailEngine extends
		com.octo.captcha.engine.image.ImageCaptchaEngine {
	@SuppressWarnings("unchecked")
	public GmailEngine(WordGenerator wordGen, FontGenerator fontGen,
			ColorGenerator colerGen, BackgroundGenerator backGen, int min,
			int max, float radius) {
		TextPaster randomPaster = new GlyphsPaster(min, max, colerGen,
				new GlyphsVisitors[] {
						new TranslateGlyphsVerticalRandomVisitor(1),
						new OverlapGlyphsUsingShapeVisitor(3),
						new TranslateAllToRandomPointVisitor() });

		PinchFilter pinch = new PinchFilter();

		pinch.setAmount(-.5f);
		pinch.setRadius(radius);
		pinch.setAngle((float) (Math.PI / 16));
		pinch.setCentreX(0.5f);
		pinch.setCentreY(-0.01f);
		pinch.setEdgeAction(ImageFunction2D.CLAMP);

		PinchFilter pinch2 = new PinchFilter();
		pinch2.setAmount(-.6f);
		pinch2.setRadius(radius);
		pinch2.setAngle((float) (Math.PI / 16));
		pinch2.setCentreX(0.3f);
		pinch2.setCentreY(1.01f);
		pinch2.setEdgeAction(ImageFunction2D.CLAMP);

		PinchFilter pinch3 = new PinchFilter();
		pinch3.setAmount(-.6f);
		pinch3.setRadius(radius);
		pinch3.setAngle((float) (Math.PI / 16));
		pinch3.setCentreX(0.8f);
		pinch3.setCentreY(-0.01f);
		pinch3.setEdgeAction(ImageFunction2D.CLAMP);

		List<ImageDeformation> textDef = new ArrayList<ImageDeformation>();
		textDef.add(new ImageDeformationByBufferedImageOp(pinch));
		textDef.add(new ImageDeformationByBufferedImageOp(pinch2));
		textDef.add(new ImageDeformationByBufferedImageOp(pinch3));

		com.octo.captcha.component.image.wordtoimage.WordToImage word2image;
		word2image = new DeformedComposedWordToImage(false, fontGen, backGen,
				randomPaster, new ArrayList<ImageDeformation>(),
				new ArrayList<ImageDeformation>(), textDef);
		factories.add(new com.octo.captcha.image.gimpy.GimpyFactory(wordGen,
				word2image, false));
	}

	public GmailEngine() {
		this(new RandomWordGenerator("ABCDEGHJKLMNRSTUWXY235689"),
				new RandomFontGenerator(40, 40, new Font[] {
						new Font("nyala", Font.BOLD, 40),
						new Font("Bell MT", Font.PLAIN, 40),
						new Font("Credit valley", Font.BOLD, 40) }, false),
				new RandomListColorGenerator(new Color[] {
						new Color(23, 170, 27), new Color(220, 34, 11),
						new Color(23, 67, 172) }),
				new UniColorBackgroundGenerator(150, 40, Color.white), 5, 6, 30);
	}
}
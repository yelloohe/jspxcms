package com.jspxcms.common.captcha;

import com.octo.captcha.engine.CaptchaEngine;
import com.octo.captcha.engine.image.gimpy.DefaultGimpyEngine;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaStore;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;

/**
 * MyManageableImageCaptchaService
 * 
 * @author liufang
 * 
 */
public class MyManageableImageCaptchaService extends
		DefaultManageableImageCaptchaService implements MyImageCaptchaService {
	/**
	 * Construct a new ImageCaptchaService with a
	 * {@link FastHashMapCaptchaStore} and a {@link DefaultGimpyEngine}
	 * minGuarantedStorageDelayInSeconds = 180s maxCaptchaStoreSize = 100000
	 * captchaStoreLoadBeforeGarbageCollection=75000
	 */
	public MyManageableImageCaptchaService() {
		super(new FastHashMapCaptchaStore(), new DefaultGimpyEngine(), 180,
				100000, 75000);
	}

	/**
	 * Construct a new ImageCaptchaService with a
	 * {@link FastHashMapCaptchaStore} and a {@link DefaultGimpyEngine}
	 * 
	 * @param minGuarantedStorageDelayInSeconds
	 * @param maxCaptchaStoreSize
	 * @param captchaStoreLoadBeforeGarbageCollection
	 */
	public MyManageableImageCaptchaService(
			int minGuarantedStorageDelayInSeconds, int maxCaptchaStoreSize,
			int captchaStoreLoadBeforeGarbageCollection) {
		super(new FastHashMapCaptchaStore(), new DefaultGimpyEngine(),
				minGuarantedStorageDelayInSeconds, maxCaptchaStoreSize,
				captchaStoreLoadBeforeGarbageCollection);
	}

	/**
	 * @param captchaStore
	 * @param captchaEngine
	 * @param minGuarantedStorageDelayInSeconds
	 * @param maxCaptchaStoreSize
	 * @param captchaStoreLoadBeforeGarbageCollection
	 */
	public MyManageableImageCaptchaService(CaptchaStore captchaStore,
			CaptchaEngine captchaEngine, int minGuarantedStorageDelayInSeconds,
			int maxCaptchaStoreSize, int captchaStoreLoadBeforeGarbageCollection) {
		super(captchaStore, captchaEngine, minGuarantedStorageDelayInSeconds,
				maxCaptchaStoreSize, captchaStoreLoadBeforeGarbageCollection);
	}

	public Boolean tryResponseForID(String ID, Object response)
			throws CaptchaServiceException {
		return tryResponseForID(ID, response, false);
	}

	public Boolean tryResponseForID(String ID, Object response,
			boolean removeOnError) throws CaptchaServiceException {
		if (!store.hasCaptcha(ID)) {
			throw new CaptchaServiceException(
					"Invalid ID, could not validate unexisting or already validated captcha");
		} else {
			Boolean valid = store.getCaptcha(ID).validateResponse(response);
			if (removeOnError) {
				store.removeCaptcha(ID);
			}
			return valid;
		}
	}
}

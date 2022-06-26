package com.tsp.new_tsp_project.api.common.image.service.impl;

import com.tsp.new_tsp_project.api.admin.mapper.ImageMapper;
import com.tsp.new_tsp_project.api.common.domain.dto.CommonImageDTO;
import com.tsp.new_tsp_project.api.common.image.service.ImageService;
import com.tsp.new_tsp_project.common.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service("ImageService")
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageMapper imageMapper;

	/**
	 * 업로드 경로
	 **/
	@Value("${image.uploadPath}")
	private String uploadPath;

	/**
	 * <pre>
	 * 1. MethodName : currentDate
	 * 2. ClassName  : ImageServiceImpl.java
	 * 3. Comment    : 현재 날짜 구하기
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 06. 02.
	 * </pre>
	 *
	 * @return
	 */
	public String currentDate() {
		// 현재 날짜 구하기
		String rtnStr = "";
		String pattern = "MMddHHmmssSSS";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}

	/**
	 * <pre>
	 * 1. MethodName : uploadImageFile
	 * 2. ClassName  : ImageServiceImpl.java
	 * 3. Comment    : 이미지 파일 등록 & 업로드
	 * 4. 작성자       : CHO
	 * 5. 작성일       : 2021. 06. 02.
	 * </pre>
	 *
	 */
	public String uploadImageFile(CommonImageDTO commonImageDTO,
								  MultipartFile[] files,
								  String flag) throws Exception {

		// 파일 확장자
		String ext;
		// 파일명
		String fileId;
		// 파일 Mask
		String fileMask;
		// 파일 크기
		long fileSize;

		int mainCnt = 0;

		File dir = new File(uploadPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		if (files != null) {
			if ("update".equals(flag)) {
				if ("production".equals(commonImageDTO.getTypeName())) {
					commonImageDTO.setImageType("main");
					commonImageDTO.setTypeIdx(commonImageDTO.getIdx());
					imageMapper.deleteImageFile(commonImageDTO);
				}
			}
			for (MultipartFile file : files) {
				try {
					ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
					fileId = currentDate();
					fileMask = fileId + '.' + ext;
					fileSize = file.getSize();

					if (!new File(uploadPath).exists()) {
						try {
							new File(uploadPath).mkdir();
						} catch (Exception e) {
							e.getStackTrace();
						}
					}

					if ("insert".equals(flag)) {
						if (mainCnt == 0) {
							commonImageDTO.setImageType("main");
						} else {
							commonImageDTO.setImageType("sub" + mainCnt);
						}
					} else {
						if ("production".equals(commonImageDTO.getTypeName())) {
							commonImageDTO.setImageType("main");
						} else {
							if (imageMapper.selectSubCnt(commonImageDTO) == 1) {
								commonImageDTO.setImageType("main");
							} else {
								commonImageDTO.setImageType("sub" + StringUtil.getInt(imageMapper.selectSubCnt(commonImageDTO), 0));
								commonImageDTO.setFileNum(StringUtil.getInt(imageMapper.selectSubCnt(commonImageDTO), 0));
							}
						}
					}

					String filePath = uploadPath + fileMask;
					file.transferTo(new File(filePath));

					Runtime.getRuntime().exec("chmod -R 755 " + filePath);

					commonImageDTO.setFileNum(StringUtil.getInt(imageMapper.selectSubCnt(commonImageDTO), 0));
					commonImageDTO.setFileName(file.getOriginalFilename());                   // 파일명
					commonImageDTO.setFileSize(fileSize);  // 파일Size
					commonImageDTO.setFileMask(fileMask);                                        // 파일Mask
					commonImageDTO.setFilePath(uploadPath + fileMask);
					commonImageDTO.setVisible("Y");

					// 이미지 정보 insert
					if (imageMapper.addImageFile(commonImageDTO) > 0) {
						mainCnt++;
					}

				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception();
				}
			}
		}

		return "Y";
	}

	@Override
	public String updateMultipleFile(CommonImageDTO commonImageDTO, MultipartFile[] files, Map<String, Object> modelMap) throws Exception {
		// 파일 확장자
		String ext;
		// 파일명
		String fileId;
		// 파일 Mask
		String fileMask;
		// 파일 크기
		long fileSize;

		String[] arrayState = (String[]) modelMap.get("arrayState");
		String[] arrayIdx = (String[]) modelMap.get("arrayIdx");

		File dir = new File(uploadPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		int fileCnt = 0;

		try {
			for (int i = 0; i < arrayState.length; i++) {
				if ("U".equals(arrayState[i])) {
					if (files[fileCnt] != null) {
						ext = files[fileCnt].getOriginalFilename().substring(files[fileCnt].getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
						fileId = currentDate();
						fileMask = fileId + '.' + ext;
						fileSize = files[fileCnt].getSize();

						if (!new File(uploadPath).exists()) {
							try {
								new File(uploadPath).mkdir();
							} catch (Exception e) {
								e.getStackTrace();
							}
						}

						String filePath = uploadPath + fileMask;
						files[fileCnt].transferTo(new File(filePath));

						Runtime.getRuntime().exec("chmod -R 755 " + filePath);

						if (i == 0) {
							commonImageDTO.setFileNum(0);
							commonImageDTO.setVisible("N");
							commonImageDTO.setImageType("main");// 파일Mask
							imageMapper.deleteImageFile(commonImageDTO);
						} else {
							commonImageDTO.setFileNum(StringUtil.getInt(imageMapper.selectSubCnt(commonImageDTO), 0));
							commonImageDTO.setImageType("sub" + StringUtil.getInt(imageMapper.selectSubCnt(commonImageDTO), 0));// 파일Mask
						}
						commonImageDTO.setFileName(files[fileCnt].getOriginalFilename());                   // 파일명
						commonImageDTO.setFileSize(fileSize);  // 파일Size
						commonImageDTO.setFileMask(fileMask);
						commonImageDTO.setFilePath(uploadPath + fileMask);

						// 이미지 정보 insert
						if (imageMapper.addImageFile(commonImageDTO) > 0) {
						}
					}
					fileCnt++;
				} else if ("D".equals(arrayState[i]) || "H".equals(arrayState[i])) {
					commonImageDTO.setIdx(StringUtil.getInt(arrayIdx[i], 0));
					imageMapper.deleteImageFile(commonImageDTO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception();
		}

		return "Y";
	}
}

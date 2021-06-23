package com.kilcote.controller.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kilcote.common.constants.StringConstant;
import com.kilcote.common.data.theme.RootData;
import com.kilcote.common.data.theme.ThemePaletteData;
import com.kilcote.controller._base.BaseController;
import com.kilcote.controller._base.annotation.ControllerEndpoint;
import com.kilcote.entity.system.ThemePalette;
import com.kilcote.service.system.ThemeService;
import com.kilcote.utils.CsvUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
@RestController
@RequestMapping({"/api/theme"})
@RequiredArgsConstructor
public class ThemeController extends BaseController {

	@Autowired
	private ThemeService service;
	
	@GetMapping("/getTotalThemes")
	public ResponseEntity<RootData> getTotalThemes() {
		RootData result = this.service.listThemeBean();
		return new ResponseEntity<RootData>(result, HttpStatus.OK);
	}
	
	@PostMapping(value = "/add")
	@PreAuthorize("hasAuthority('theme:add')")
	@ControllerEndpoint(operation="Add theme", exceptionMessage="Add theme failed")
	public ResponseEntity<ThemePaletteData> addTheme(@RequestBody ThemePaletteData theme) throws Exception {
		ThemePaletteData themeAdd = null;
		theme.setId(null);
		Long id = this.service.addTheme(theme);
		if(id != null) {
			themeAdd = this.service.findById(id);
		}
		return new ResponseEntity<ThemePaletteData>(themeAdd, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/update/{id}")
	@PreAuthorize("hasAuthority('theme:update')")
	@ControllerEndpoint(operation="Update theme", exceptionMessage="Update theme failed")
	public ResponseEntity<ThemePaletteData> updateTheme(@PathVariable Long id, @RequestBody ThemePaletteData theme) throws Exception {
		ThemePaletteData themeUpdate = null;
		theme.setId(id);
		if(this.service.updateTheme(theme)) {
			themeUpdate = this.service.findById(theme.getId());
		}
		return new ResponseEntity<ThemePaletteData>(themeUpdate, HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('theme:delete')")
	@ControllerEndpoint(operation="Delete theme", exceptionMessage="Delete theme failed")
    public ResponseEntity<Boolean> deleteTheme(@PathVariable Long id) {
        Boolean result = this.service.deleteTheme(id);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
	
	@DeleteMapping("/deletes/{ids}")
	@PreAuthorize("hasAuthority('theme:delete')")
	@ControllerEndpoint(operation="Delete themes", exceptionMessage="Delete themes failed")
    public ResponseEntity<Boolean> deleteThemes(@PathVariable String ids) {
		String[] themeIds = ids.split(StringConstant.COMMA);
		Boolean result = this.service.deleteByIds(themeIds);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }
	
	@GetMapping("/download")
	public void download(@RequestParam Map<String, String> requestParams, HttpServletResponse response) throws Exception {
        String filename = "theme.csv";
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
		CsvUtil.writeFromDatas(service.listTheme(), response.getWriter(), ThemePalette.class);
	}
	
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
		Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		List<ThemePalette> themePaletteList = CsvUtil.convertFromCsv(file, ThemePalette.class);
		
		for (ThemePalette csvTheme : themePaletteList) {
			System.out.println("ThemeName: " + csvTheme.getThemeName());
            System.out.println("ThemeKey: " + csvTheme.getThemeKey());
            System.out.println("==========================");
		}
		return "success";
	}
	
//	public ResponseEntity<Resource> download(@RequestParam Map<String, String> requestParams) throws Exception {
//		
//		File file = new File("");
//		Path path = Paths.get(file.getPath());			
//		byte[] data = new byte[0];
//		ByteArrayResource resource = new ByteArrayResource(data);
//		HttpHeaders headers = new HttpHeaders();
//		
//		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);//parseMediaType("application/csv")
//		headers.add("Content-Disposition", "filename=" + "theme.csv");
//		
//		return ResponseEntity.ok()
//				.headers(headers)
//				.contentLength(file.length())
//				.body(resource);
//	}

//		byte[] data = Files.readAllBytes(path);
//		headers.add("Access-Control-Allow-Origin", "*");
//		headers.add("Access-Control-Allow-Methods", "*");
//		headers.add("Access-Control-Allow-Headers", "Content-Type");
//		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//		headers.add("Pragma", "no-cache");
//		headers.add("Expires", "0");
}
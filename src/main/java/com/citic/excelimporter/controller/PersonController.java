package com.citic.excelimporter.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.citic.excelimporter.aspect.Upload;
import com.citic.excelimporter.aspect.UploadType;
import com.citic.excelimporter.common.R;
import com.citic.excelimporter.decryption.DecryptData;
import com.citic.excelimporter.decryption.DecryptionContextHolder;
import com.citic.excelimporter.exception.DataValidationException;
import com.citic.excelimporter.pojo.Person;
import com.citic.excelimporter.service.PersonService;
import com.citic.excelimporter.utils.ExcelUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

/**
 * @author jiaoyuanzhou
 */
@RequestMapping("/person")
@RestController
public class PersonController {

    @Resource
    private PersonService personService;

    private static final String UPLOAD_DIR = "uploads/";


    @PostMapping("/importData")
    public R<Object> importData(@RequestParam("file") MultipartFile file) throws DataValidationException, IOException {
        List<Person> people = ExcelUtils.readExcel(file.getInputStream());
        personService.importData(people);
        return R.success();
    }


    /**
     * 指定模板数据导入数据库表(加密)
     *
     * @param encryptedFile
     * @return
     */
    @DecryptData
    @PostMapping("/importEncryptData")
    public R<Object> importEncryptData(@RequestParam("file") MultipartFile encryptedFile) throws DataValidationException {
        try {
            // 解密后的数据从上下文中获取
            byte[] decryptedData = DecryptionContextHolder.getDecryptedData();

            // 使用POI解析解密后的Excel文件
            try (InputStream decryptedStream = new ByteArrayInputStream(decryptedData)) {
                List<Person> people = ExcelUtils.readExcel(decryptedStream);
                personService.importData(people);
            }
            return R.success();
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("解析文件发生错误");
        }

    }

    /**
     * 多文件上传功能，因时间紧迫未进行多文件加密，原理和文件加密一样
     *
     * @param encryptedFiles
     * @return
     * @throws IOException
     * @throws DataValidationException
     */
    @PostMapping("/importFiles")
    public R<Object> importFiles(@RequestParam("files") MultipartFile[] encryptedFiles) throws IOException, DataValidationException {
        for (MultipartFile encryptedFile : encryptedFiles) {
            List<Person> people = ExcelUtils.readExcel(encryptedFile.getInputStream());
            personService.importData(people);
        }
        return R.success();
    }

    /**
     * 将大文件进行分片上传，每个分片大小为1M
     *
     * @param chunk
     * @param filename
     * @param totalSize
     * @param index
     * @return
     */
    @PostMapping("/uploadChunks")
    public R<String> handleFileUpload(
            @RequestParam("chunk") MultipartFile chunk,
            @RequestParam("filename") String filename,
            @RequestParam("totalSize") long totalSize,
            @RequestParam("index") int index) {

        try {
            // 如果目录不存在，则创建上传目录
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 根据原始文件名和索引生成每个分片的唯一文件名
            String uniqueFilename = filename + "_" + index;
            File chunkFile = new File(uploadDir, uniqueFilename);

            // 将分片保存到服务器
            try (FileOutputStream fos = new FileOutputStream(chunkFile, true)) {
                fos.write(chunk.getBytes());
            }

            // 检查是否已接收到所有分片
            if (index == totalSize - 1) {
                // 所有分片已接收，可以在这里执行任何必要的处理


                // 返回成功响应
                return R.success("文件上传成功");
            }

            // 如果并非所有分片都已接收，则返回部分成功响应
            return R.success("分片上传成功");

        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("上传分片时出错");
        }
    }

    @Upload(type = UploadType.XLSX)
    @PostMapping("/syncImport")
    public R<Object> syncImport(@RequestParam("file") MultipartFile file) throws IOException, DataValidationException {
        List<Person> people = ExcelUtils.readExcel(file.getInputStream());
        personService.importData(people);
        return R.success();
    }

    /**
     * 添加或者更新信息，ID存在就更新，不存在就执行添加操作
     *
     * @param person
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public R<Object> saveOrUpdatePerson(@RequestBody Person person) {
        personService.saveOrUpdatePerson(person);
        return R.success();
    }

    /**
     * 根据ID查信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Object> getPerson(@PathVariable Long id) {
        return R.success(personService.getById(id));
    }

    /**
     * 根据ID删除信息
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public R<Object> deletePerson(@PathVariable Long id) {
        personService.removeById(id);
        return R.success();
    }

    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/all")
    public R<Object> getAllPeopleWithPagination(@RequestParam(defaultValue = "1") long currentPage,
                                                @RequestParam(defaultValue = "10") long pageSize) {
        IPage<Person> personPage = personService.getPeopleWithPagination(currentPage, pageSize);

        return R.success(personPage);
    }


}
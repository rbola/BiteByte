package com.main.bitebyte;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.main.bitebyte.common.RepositoryConfig;


@SpringBootTest
@Import(RepositoryConfig.class)
class BiteByteApplicationTests {

}

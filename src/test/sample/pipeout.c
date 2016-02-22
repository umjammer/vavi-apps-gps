#include <stdio.h>

#include <uusbd.h>
#include <process.h>    /* _beginthread, _endthread */

#define NUM_INTERFACE	0  // out ����C���^�[�t�F�[�X�̔ԍ�
#define NUM_PIPE	0  // out ����p�C�v�̔ԍ�
#define LOOPCOUNT	10 // �J��Ԃ���

void outpipe(UCHAR ifnum, UCHAR pipenum, DWORD loopcount, char *buf, int len) {
    HUSB husb;
    HANDLE h;
    DWORD size, ret, i, j, maxlen;
    husb = Uusbd_Open();
    if (husb == INVALID_HANDLE_VALUE) {
printf("Uusbd.sys���g����USB�f�o�C�X�͌�����܂���\n");
        return;
    }
printf("usb: %d\n", husb);
    ret = Uusbd_Check(husb);
    if (ret != UU_CHECK_OK) {
printf("IOCTL�G���[\n");
        return;
    }

    char *_buf = "!PUON\r\n";
    len = 8;
    
printf("buf: %s, %d\n", _buf, len);
    h = Uusbd_OpenPipe(husb, ifnum, pipenum);
printf("pipe: %d\n", h);
    Uusbd_ResetDevice(husb);

    ret = WriteFile(h, _buf, len, &size, NULL);
printf("write ret=%d size=%d\n", ret, size);
    if (ret ==0 || size == 0) {
        ret = Uusbd_Check(husb);
        if (ret != UU_CHECK_OK) {
printf("�f�o�C�X�����O����܂���\n");
        }
    }
    
    CloseHandle(h);
    Uusbd_Close(husb);
    free(buf);
}

void test() {
    HUSB husb = Uusbd_Open();
    if (husb == INVALID_HANDLE_VALUE) {
printf("Uusbd.sys���g����USB�f�o�C�X�͌�����܂���\n");
        return;
    }
printf("usb: %d\n", husb);

    int ret = Uusbd_Check(husb);
    if (ret != UU_CHECK_OK) {
printf("IOCTL�G���[\n");
        return;
    }

    int i;
    for (i = 0; i < 255; i++) {
        HANDLE h = Uusbd_OpenPipe(husb, 1, i);
printf("pipe[%d]: %d\n", i, h);
        CloseHandle(h);
    }
}

int main(int args, const char *argv[]) {
    // �w�肵���p�C�v�̓��e���_���v����
printf("arg[1]: %s\n", argv[1]);
    outpipe(NUM_INTERFACE, NUM_PIPE, LOOPCOUNT, argv[1], strlen(argv[1]));
    return 0;
}

/* */

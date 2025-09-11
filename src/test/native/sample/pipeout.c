#include <stdio.h>

#include <uusbd.h>
#include <process.h>    /* _beginthread, _endthread */

#define NUM_INTERFACE	0  // The interface number to send out
#define NUM_PIPE	0  // Pipe number to output
#define LOOPCOUNT	10 // Number of repetitions

void outpipe(UCHAR ifnum, UCHAR pipenum, DWORD loopcount, char *buf, int len) {
    HUSB husb;
    HANDLE h;
    DWORD size, ret, i, j, maxlen;
    husb = Uusbd_Open();
    if (husb == INVALID_HANDLE_VALUE) {
printf("No USB devices using Uusbd.sys found\n");
        return;
    }
printf("usb: %d\n", husb);
    ret = Uusbd_Check(husb);
    if (ret != UU_CHECK_OK) {
printf("IOCTL Error\n");
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
printf("Device Removed\n");
        }
    }
    
    CloseHandle(h);
    Uusbd_Close(husb);
    free(buf);
}

void test() {
    HUSB husb = Uusbd_Open();
    if (husb == INVALID_HANDLE_VALUE) {
printf("No USB devices using Uusbd.sys found\n");
        return;
    }
printf("usb: %d\n", husb);

    int ret = Uusbd_Check(husb);
    if (ret != UU_CHECK_OK) {
printf("IOCTL Error\n");
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
    // Dumps the contents of the specified pipe
printf("arg[1]: %s\n", argv[1]);
    outpipe(NUM_INTERFACE, NUM_PIPE, LOOPCOUNT, argv[1], strlen(argv[1]));
    return 0;
}

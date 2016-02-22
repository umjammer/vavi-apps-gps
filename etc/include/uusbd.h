/***************************************************

Copyright (c) 2000-2001  kashiwano masahiro

    UUSBD.DLL include file Version 2.0
    
****************************************************/

#include <windows.h>
#include "usb200.h"

#define UU_MASK_NO			(0)
#define UU_MASK_CLASS		(1<<0)
#define UU_MASK_SUBCLASS	(1<<1)
#define UU_MASK_VENDOR		(1<<2)
#define UU_MASK_PRODUCT		(1<<3)
#define UU_MASK_BCDDEVICE	(1<<4)

#define UU_CHECK_OK			0
#define UU_CHECK_NOTOPEN	1
#define UU_CHECK_NODEVICE	2

#define UU_RECIPIENT_DEVICE	0
#define UU_RECIPIENT_INTERFACE	1
#define UU_RECIPIENT_ENDPOINT	2
#define UU_RECIPIENT_OTHER		3	


typedef void* HUSB;

#ifdef  __cplusplus
extern "C" {
#endif

// Open Close�֌W
HUSB   APIENTRY Uusbd_Open_mask(ULONG flag, UCHAR Class, UCHAR SubClass, USHORT Vendor, USHORT Product, BYTE bcdDevice);
HUSB   APIENTRY Uusbd_Open(void);
void   APIENTRY Uusbd_Close(HUSB husb);
HANDLE APIENTRY Uusbd_OpenPipe(HUSB husb, UCHAR interface_num, UCHAR pipe_num);
HANDLE APIENTRY Uusbd_OpenPipe_Overlapped(HUSB husb, UCHAR interface_num, UCHAR pipe_num);

DWORD  APIENTRY Uusbd_Check(HUSB husb);
BOOL   APIENTRY Uusbd_AbortPipe(HANDLE handle);
BOOL   APIENTRY Uusbd_ResetPipe(HANDLE handle);
BOOL   APIENTRY Uusbd_ResetDevice(HUSB husb);

// �W�����N�G�X�g
BOOL   APIENTRY Uusbd_GetDeviceDescriptor(HUSB husb, PUSB_DEVICE_DESCRIPTOR descriptor);
BOOL   APIENTRY Uusbd_GetDeviceQualifierDescriptor(HUSB husb, PUSB_DEVICE_QUALIFIER_DESCRIPTOR descriptor);
BOOL   APIENTRY Uusbd_GetConfigurationDescriptor(HUSB husb, char *buf, DWORD siz);
BOOL   APIENTRY Uusbd_GetOtherSpeedConfigurationDescriptor(HUSB husb, char *buf, DWORD siz);
BOOL   APIENTRY Uusbd_GetStatusEndpoint(HUSB husb, USHORT endp_num, USHORT *status);

// �f�o�C�X�A�N���X���N�G�X�g
BOOL   APIENTRY Uusbd_ClassRequest(HUSB husb, BOOL dir_in, UCHAR recipient, UCHAR bRequest, USHORT wValue, USHORT wIndex, USHORT wLength, char *data);
BOOL   APIENTRY Uusbd_VendorRequest(HUSB husb, BOOL dir_in, UCHAR recipient, UCHAR bRequest, USHORT wValue, USHORT wIndex, USHORT wLength, char *data);

#ifdef  __cplusplus
}
#endif

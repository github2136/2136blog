## NFC读写
首先添加NFC权限`<uses-permission android:name="android.permission.NFC" />`，`<uses-feature android:name="android.hardware.nfc" android:required="true" />`，NFC读取分为三种类型`ACTION_NDEF_DISCOVERED`，` ACTION_TECH_DISCOVERED`，`ACTION_TAG_DISCOVERED`。
* ACTION_NDEF_DISCOVERED 表示NDEF数据类型
```xml
<intent-filter>
    <action android:name="android.nfc.action.NDEF_DISCOVERED"/>
    <category android:name="android.intent.category.DEFAULT"/>
    <data android:mimeType="text/plain" />
</intent-filter>
```
```xml
<intent-filter>
    <action android:name="android.nfc.action.NDEF_DISCOVERED"/>
    <category android:name="android.intent.category.DEFAULT"/>
   <data android:scheme="http"
              android:host="developer.android.com"
              android:pathPrefix="/index.html" />
</intent-filter>
```
* ACTION_TECH_DISCOVERED 表示按技术类型读取
获取到tag后可以使用`getTechList()`来显示该标签支持的技术
```xml
<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
    <tech-list>
        <tech>android.nfc.tech.IsoDep</tech>
        <tech>android.nfc.tech.NfcA</tech>
        <tech>android.nfc.tech.NfcB</tech>
        <tech>android.nfc.tech.NfcF</tech>
        <tech>android.nfc.tech.NfcV</tech>
        <tech>android.nfc.tech.Ndef</tech>
        <tech>android.nfc.tech.NdefFormatable</tech>
        <tech>android.nfc.tech.MifareClassic</tech>
        <tech>android.nfc.tech.MifareUltralight</tech>
    </tech-list>
</resources>
```
```xml
<activity>
...
<intent-filter>
    <action android:name="android.nfc.action.TECH_DISCOVERED"/>
</intent-filter>

<meta-data android:name="android.nfc.action.TECH_DISCOVERED"
    android:resource="@xml/nfc_tech_filter" />
...
</activity>
```
* ACTION_TAG_DISCOVERED 如果前两种都没触发那么将会触发该类型
```xml
<intent-filter>
    <action android:name="android.nfc.action.TAG_DISCOVERED"/>
</intent-filter>
```

## intent中的信息
* EXTRA_TAG (必有): 标签信息
* EXTRA_NDEF_MESSAGES (可选): NDEF信息使用`ACTION_NDEF_DISCOVERED`获取
* EXTRA_ID (可选): 标签ID

## NDEF
略

## 动态注册监听
```java
public abstract class BaseNFCActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] techList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mPendingIntent = PendingIntent.getActivity(this, 1,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_UPDATE_CURRENT);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "手机不支持NFC功能", Toast.LENGTH_SHORT).show();
        } else if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC功能尚未开启", Toast.LENGTH_SHORT).show();
        }
        techList = new String[][]{
                new String[]{android.nfc.tech.MifareClassic.class.getName()},
                new String[]{android.nfc.tech.MifareUltralight.class.getName()},
                new String[]{android.nfc.tech.IsoDep.class.getName()},
                new String[]{android.nfc.tech.Ndef.class.getName()},
                new String[]{android.nfc.tech.NdefFormatable.class.getName()},
                new String[]{android.nfc.tech.NfcA.class.getName()},
                new String[]{android.nfc.tech.NfcB.class.getName()},
                new String[]{android.nfc.tech.NfcF.class.getName()},
                new String[]{android.nfc.tech.NfcV.class.getName()}
        };
        IntentFilter tech01 = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tech01.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }

        IntentFilter tech02 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter tech03 = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        // 生成intentFilter
        mFilters = new IntentFilter[]{tech01, tech02, tech03};
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters, techList);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switch (intent.getAction()) {
            case NfcAdapter.ACTION_NDEF_DISCOVERED: {
            }
            break;
            case NfcAdapter.ACTION_TAG_DISCOVERED: {
            }
            break;
            case NfcAdapter.ACTION_TECH_DISCOVERED: {
            }
            break;
        }
    }
    String byteArrayToHexString(byte[] bytes) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                out.append(0);
            }
            out.append(hv);
        }
        return out.toString();
    }

    byte[] hexStringtoByteArray(String str) {
        str = str.toLowerCase();
        String hexStr = "0123456789abcdef";
        if (str.length() % 2 == 0) {
            byte[] bytes = new byte[str.length() / 2];
            for (int i = 0; i < bytes.length; i++) {
                String hex1 = str.substring(i * 2, i * 2 + 1);
                String hex2 = str.substring(i * 2 + 1, i * 2 + 2);
                int hexStr1 = hexStr.indexOf(hex1);
                int hexStr2 = hexStr.indexOf(hex2);
                int height = hexStr1 << 4 & 0xF0;
                int low = hexStr2 & 0x0F;
                bytes[i] = (byte) ((height | low) & 0xFF);
            }
            return bytes;
        } else {
            return null;
        }
    }
}

```
## MifareUltralight读写
https://www.nxp.com/cn/products/identification-and-security/mifare-ics/mifare-ultralight:MC_53452
MIFARE Ultralight 由64字节组成，每页4字节，前4页不可写，后12页可读可写，一共可写入48字节  
MIFARE Ultralight C 由192字节组成，每页4字节，前4页不可写，中间36页可读可写，然后4页是计数器等信息，可读，最后四页用于验证密钥不可读，一共可写入144字节
每次只能写入一页数据
* 读取
```java
public void readMifareUltralight(Tag tag) {
    MifareUltralight mifare = MifareUltralight.get(tag);
    try {
        mifare.connect();
        int size = mifare.PAGE_SIZE;
        byte[] p = mifare.readPages(0);

        tvInfo.append("page0-3 : " + byteArrayToHexString(p) + "\n");

        //一次读取4页
        switch (mifare.getType()) {
            case MifareUltralight.TYPE_ULTRALIGHT: {
                for (int i = 0; i < 3; i++) {
                    byte[] p1 = mifare.readPages(i * 4 + 4);
                    tvInfo.append(String.format("page%d-%d :%s \n", i * 4 + 4, i * 4 + 4 + 3, new String(p1, Charset.forName("UTF-8"))));
                }
                byte[] bytes = new byte[64];
                for (int i = 0; i < 3; i++) {
                    byte[] p1 = mifare.readPages(i * 4 + 4);
                    System.arraycopy(p1, 0, bytes, i * 4 * 4, 4 * 4);
                }
                tvInfo.append(String.format("%s \n", new String(bytes, Charset.forName("UTF-8"))));
            }
            break;
            case MifareUltralight.TYPE_ULTRALIGHT_C: {
                for (int i = 0; i < 9; i++) {
                    byte[] p1 = mifare.readPages(i * 4 + 4);
                    tvInfo.append(String.format("page%d-%d :%s \n", i * 4 + 4, i * 4 + 4 + 3, new String(p1, Charset.forName("UTF-8"))));
                }
                byte[] bytes = new byte[144];
                for (int i = 0; i < 9; i++) {
                    byte[] p1 = mifare.readPages(i * 4 + 4);
                    System.arraycopy(p1, 0, bytes, i * 4 * 4, 4 * 4);
                }
                tvInfo.append(String.format("%s \n", new String(bytes, Charset.forName("UTF-8"))));
            }
            break;
        }
    } catch (IOException e) {
        tvInfo.append("读取失败！");
    } catch (Exception ee) {
        tvInfo.append("读取失败！");
    } finally {
        if (mifare != null) {
            try { mifare.close(); } catch (IOException e) { }
        }
    }
}
```
* 写入
```java
public void writeMifareUltralight(Tag tag, String text) {
    MifareUltralight ultralight = MifareUltralight.get(tag);
    if (ultralight != null) {
        try {
            ultralight.connect();
            switch (ultralight.getType()) {
                case MifareUltralight.TYPE_ULTRALIGHT: {
                    byte[] baseData = new byte[48];

                    byte[] bytes = text.getBytes(Charset.forName("UTF-8"));
                    if (bytes.length > 48) {
                        Toast.makeText(this, "写入内容不得超过48字节", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.arraycopy(bytes, 0, baseData, 0, bytes.length);
                    for (int i = 0; i < 12; i++) {
                        byte[] d = new byte[4];

                        System.arraycopy(baseData, i * 4, d, 0, 4);
                        ultralight.writePage(i + 4, d);
                        Log.e("i", i + "");
                    }
                    Toast.makeText(this, "写入完成", Toast.LENGTH_SHORT).show();
                }
                break;
                case MifareUltralight.TYPE_ULTRALIGHT_C: {
                    byte[] baseData = new byte[144];

                    byte[] bytes = text.getBytes(Charset.forName("UTF-8"));
                    if (bytes.length > 144) {
                        Toast.makeText(this, "写入内容不得超过144字节", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.arraycopy(bytes, 0, baseData, 0, bytes.length);
                    for (int i = 0; i < 36; i++) {
                        byte[] d = new byte[4];

                        System.arraycopy(baseData, i * 4, d, 0, 4);
                        ultralight.writePage(i + 4, d);
                        Log.e("i", i + "");
                    }
                    Toast.makeText(this, "写入完成", Toast.LENGTH_SHORT).show();
                }
                break;
                case MifareUltralight.TYPE_UNKNOWN: {
                    Toast.makeText(this, "未知类型", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        } catch (IOException e) {
            Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                ultralight.close();
            } catch (IOException e) {

            }
        }
    }
}
```
## MifareClassic读写
https://www.nxp.com/cn/products/identification-and-security/mifare-ics/mifare-classic:MC_41863
MifareClassic标签将数据分为多个扇区(sector)，每个扇区分为多个块(block)，每块固定为16字节(byte)
标签类型
 mini 320byte，  5个扇区每扇区4块
 1k    1024byte，16个扇区每扇区4块
 2k    2048byte，32个扇区每扇区4块
 4k    4096byte，前32个扇区每扇区4块，后8个扇区每个扇区16块

第0扇区的第0块为制造商信息，无法修改
每个扇区最后一块为密码和访问权限内容，前6位为A密码，后6位为B密码（可选，如不需要可以当做读写块使用），中间4位为读写条件
每块可当读写块(Data blocks)或值块(Value block)使用，值块可当做电子钱包使用
* 读取读写块(Data blocks)
读取必须使用keyA或keyB授权后按权限来读取或写入数据
```java
void readMifareClassic(Tag tag) {
    MifareClassic mfc = MifareClassic.get(tag);
    if (mfc != null) {
        try {
            mfc.connect();
            int type = mfc.getType();
            //获取TAG的类型
            int sectorCount = mfc.getSectorCount();
            //获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            tvInfo.append("卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共" + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n");
            boolean auth;
            for (int j = 0; j < sectorCount; j++) {
                auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
                int bCount;
                int bIndex;
                if (auth) {
                    tvInfo.append("Sector " + j + ":验证成功\n");
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        try {
                            byte[] data = mfc.readBlock(bIndex);
                            tvInfo.append("Block " + bIndex + " : " + byteArrayToHexString(data) + "\n");
                        } catch (IOException e) {
                        }
                        bIndex++;
                    }
                } else {
                    tvInfo.append("Sector " + j + ":验证失败\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```
* 写入读写块(Data blocks)
```java
void writeMifareClassic(Tag tag, byte[] bytes) {
    MifareClassic mfc = MifareClassic.get(tag);
    if (mfc != null) {
        try {
            mfc.connect();
            boolean auth;
            int sectorAddress = spSector.getSelectedItemPosition();
            auth = mfc.authenticateSectorWithKeyA(sectorAddress, MifareClassic.KEY_DEFAULT);

            if (auth) {
                int firstIndex = mfc.sectorToBlock(sectorAddress);

                mfc.writeBlock(firstIndex + spBlock.getSelectedItemPosition(), bytes);
                mfc.close();
                Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                readInfo();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mfc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```
* 值块(Value block)

值：表示带符号的4字节。值的的低位存储在前，高位在后。负值以二进制的补码格式储存，处于数据的安全和完成性原因，值存储3次，两次非反转，一次二进制反转
地址：实现备份管理，表示1字节地址，。地址存储4次两次非反转两次二进制反转，只能通过写命令修改

值块可做加值减值操作，值块必须按指定格式初始化并。**加粗表示反转值**

<table>
    <tr>
        <th>Byte Number</th>
        <th>0</th>
        <th>1</th>
        <th>2</th>
        <th>3</th>
        <th>4</th>
        <th>5</th>
        <th>6</th>
        <th>7</th>
        <th>8</th>
        <th>9</th>
        <th>10</th>
        <th>11</th>
        <th>12</th>
        <th>13</th>
        <th>14</th>
        <th>15</th>
    </tr>
    <tr>
        <td align="center">Description</td>
        <td colspan="4" align="center">value</td>
        <th colspan="4" align="center">value</th>
        <td colspan="4" align="center">value</td>
        <td>adr</td>
        <th>adr</th>
        <td>adr</td>
        <th>adr</th>
    </tr>
</table>
例如：值1234567地址为17,1234567转16进制为0012D687，17地址转16进制为11

<table>
    <tr>
        <th>Byte Number</th>
        <th>0</th>
        <th>1</th>
        <th>2</th>
        <th>3</th>
        <th>4</th>
        <th>5</th>
        <th>6</th>
        <th>7</th>
        <th>8</th>
        <th>9</th>
        <th>10</th>
        <th>11</th>
        <th>12</th>
        <th>13</th>
        <th>14</th>
        <th>15</th>
    </tr>
    <tr>
        <td align="center">Description</td>
        <td colspan="4" align="center">value</td>
        <th colspan="4" align="center">value</th>
        <td colspan="4" align="center"></td>
        <td>adr</td>
        <th>adr</th>
        <td>adr</td>
        <th>adr</th>
    </tr>
    <tr>
        <td align="center">值</td>
        <td>87</td>
        <td>D6</td>
        <td>12</td>
        <td>00</td>
        <td>78</td>
        <td>29</td>
        <td>ED</td>
        <td>FF</td>
        <td>87</td>
        <td>D6</td>
        <td>12</td>
        <td>00</td>
        <td>11</td>
        <td>EE</td>
        <td>11</td>
        <td>EE</td>
    </tr>
</table>

* 块说明

| Access Bits                                  | Valid Commands                                                     | Block | Description         |
|----------------------------------------------|--------------------------------------------------------------------|-------|---------------------|
| C1<sub>0</sub> C2<sub>0</sub> C3<sub>0</sub> | 读、写、增、减、赋值、还原<br>(read, write, increment, decrement,transfer, restore) | 0     | 块(data block)       |
| C1<sub>1</sub> C2<sub>1</sub> C3<sub>1</sub> | 读、写、增、减、赋值、还原<br>(read, write, increment, decrement,transfer, restore) | 1     | 块(data block)       |
| C1<sub>2</sub> C2<sub>2</sub> C3<sub>2</sub> | 读、写、增、减、赋值、还原<br>(read, write, increment, decrement,transfer, restore) | 2     | 块(data block)       |
| C1<sub>3</sub> C2<sub>3</sub> C3<sub>3</sub> | 读、写(read, write)                                                   | 3     | 权限块(sector trailer) |

* 权限控制说明

控制位块由3个字节控制
<table>
    <tr>
        <th>Bit</th>
        <th>0</th>
        <th>1</th>
        <th>2</th>
        <th>3</th>
        <th>4</th>
        <th>5</th>
        <th>6</th>
        <th>7</th>
    </tr>
    <tr>
        <th>Byte6</th>
        <th>C2<sub>3</sub></th>
        <th>C2<sub>2</sub></th>
        <th>C2<sub>1</sub></th>
        <th>C2<sub>0</sub></th>
        <th>C1<sub>3</sub></th>
        <th>C1<sub>2</sub></th>
        <th>C1<sub>1</sub></th>
        <th>C1<sub>0</sub></th>
    </tr>
    <tr>
        <th>Byte7</th>
        <td>C1<sub>3</sub></td>
        <td>C1<sub>2</sub></td>
        <td>C1<sub>1</sub></td>
        <td>C1<sub>0</sub></th>
        <th>C3<sub>3</sub></th>
        <th>C3<sub>2</sub></th>
        <th>C3<sub>1</sub></th>
        <th>C3<sub>0</sub></th>
    </tr>
    <tr>
        <th>Byte8</th>
        <td>C3<sub>3</sub></td>
        <td>C3<sub>2</sub></td>
        <td>C3<sub>1</sub></td>
        <td>C3<sub>0</sub></td>
        <td>C2<sub>3</sub></td>
        <td>C2<sub>2</sub></td>
        <td>C2<sub>1</sub></td>
        <td>C2<sub>0</sub></td>
    </tr>
    <tr>
        <td colspan="9" align="center">用户数据 </td>
    </tr>
</table>

* 权限控制（块3）
根据扇区尾部（块3）访问位控制读写权限
<table>
    <tr>
        <th colspan="3">Access bits</th>
        <th colspan="6">Access condition for</th>
        <th>Remark</th>
    </tr>
    <tr>
        <td colspan="3"></td>
        <td align="center" colspan="2">keyA</td>
        <td colspan="2">Access bits</td>
        <td align="center" colspan="2">keyB</td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td>C1</td>
        <td>C2</td>
        <td>C3</td>
        <td>read</td>
        <td>write</td>
        <td>read</td>
        <td>write</td>
        <td>read</td>
        <td>write</td>
        <td></td>
    </tr>
    <tr>
        <td>0</td>
        <td>0</td>
        <td>0</td>
        <td>拒绝</td>
        <td>keyA</td>
        <td>keyA</td>
        <td>拒绝</td>
        <td>keyA</td>
        <td>keyA</td>
        <td>keyB可读</td>
    </tr>
    <tr>
        <td>0</td>
        <td>1</td>
        <td>0</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td>keyA</td>
        <td>拒绝</td>
        <td>keyA</td>
        <td>拒绝</td>
        <td>keyB可读</td>
    </tr>
    <tr>
        <td>1</td>
        <td>0</td>
        <td>0</td>
        <td>拒绝</td>
        <td>keyB</td>
        <td>keyA|keyB</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td>keyB</td>
        <td></td>
    </tr>
    <tr>
        <td>1</td>
        <td>1</td>
        <td>0</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td>keyA|keyB</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td></td>
    </tr>
    <tr>
        <td>0</td>
        <td>0</td>
        <td>1</td>
        <td>拒绝</td>
        <td>keyA</td>
        <td>keyA</td>
        <td>keyA</td>
        <td>keyA</td>
        <td>keyA</td>
        <td>keyB可读，默认设置</td>
    </tr>
    <tr>
        <td>0</td>
        <td>1</td>
        <td>1</td>
        <td>拒绝</td>
        <td>keyB</td>
        <td>keyA|keyB</td>
        <td>keyB</td>
        <td>拒绝</td>
        <td>keyB</td>
        <td></td>
    </tr>
    <tr>
        <td>1</td>
        <td>0</td>
        <td>1</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td>keyA|keyB</td>
        <td>keyB</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td></td>
    </tr>
    <tr>
        <td>1</td>
        <td>1</td>
        <td>1</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td>keyA|keyB</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td>拒绝</td>
        <td></td>
    </tr>
</table>

**keyB可读时使用keyB授权无法对块进行读写**

* 数据读取（块0-2）
<table>
    <tr>
        <th colspan="3">访问位</th>
        <th colspan="4">访问条件</th>
        <th>说明</th>
    </tr>
    <tr>
        <td>C1</td>
        <td>C2</td>
        <td>C3</td>
        <td>读(read)</td>
        <td>写(write)</td>
        <td>增(increment)</td>
        <td align="center">减、还原、赋值<br>(decrement,transfer,restore)</td>
        <td></td>
    </tr>
    <tr>
        <td align="center">0</td>
        <td align="center">0</td>
        <td align="center">0</td>
        <td align="center">keyA|B</td>
        <td align="center">keyA|B</td>
        <td align="center">keyA|B</td>
        <td align="center">keyA|B</td>
        <td align="center">默认配置</td>
    </tr>
    <tr>
        <td align="center">0</td>
        <td align="center">1</td>
        <td align="center">0</td>
        <td align="center">keyA|B</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td>读写块</td>
    </tr>
    <tr>
        <td align="center">1</td>
        <td align="center">0</td>
        <td align="center">0</td>
        <td align="center">keyA|B</td>
        <td align="center">keyB</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td>读写块</td>
    </tr>    
    <tr>
        <td align="center">1</td>
        <td align="center">1</td>
        <td align="center">0</td>
        <td align="center">keyA|B</td>
        <td align="center">keyB</td>
        <td align="center">keyB</td>
        <td align="center">keyA|B</td>
        <td>值块</td>
    </tr>
    <tr>
        <td align="center">0</td>
        <td align="center">0</td>
        <td align="center">1</td>
        <td align="center">keyA|B</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td align="center">keyA|B</td>
        <td>值块</td>
    </tr>
    <tr>
        <td align="center">0</td>
        <td align="center">1</td>
        <td align="center">1</td>
        <td align="center">keyB</td>
        <td align="center">keyB</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td>读写块</td>
    </tr>
    <tr>
        <td align="center">1</td>
        <td align="center">0</td>
        <td align="center">1</td>
        <td align="center">keyB</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td>读写块</td>
    </tr>
    <tr>
        <td align="center">1</td>
        <td align="center">1</td>
        <td align="center">1</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td align="center">拒绝</td>
        <td>读写块</td>
    </tr>
</table>

* 读写权限说明
如要块设置值块并keyA可以读、减数据，可以读取控制权限，keyB可以读取、写、增、减数据，可以改keyA/B，读写控制权限

 
| 块编号    |                |                |                |
|--------|----------------|----------------|----------------|
| 块1     | C1<sub>0</sub> | C2<sub>0</sub> | C3<sub>0</sub> |
| **值1** | **1**          | **1**          | **0**          |
| 块2     | C1<sub>1</sub> | C2<sub>1</sub> | C3<sub>1</sub> |
| **值2** | **1**          | **1**          | **0**          |
| 块3     | C1<sub>2</sub> | C2<sub>2</sub> | C3<sub>2</sub> |
| **值3** | **1**          | **1**          | **0**          |
| 权限     | C1<sub>3</sub> | C2<sub>3</sub> | C3<sub>3</sub> |
| **值4** | **0**          | **1**          | **1**          |

Byte6:0000 1000&rarr;08  
Byte7:0111 0111&rarr;77  
Byte8:1000 1111&rarr;8F  

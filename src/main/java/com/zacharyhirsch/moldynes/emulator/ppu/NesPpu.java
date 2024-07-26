package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import java.util.function.UnaryOperator;

public final class NesPpu {

  public interface DrawFrame {

    void draw(byte[] frame);
  }

  private interface Tick extends UnaryOperator<NesPpu> {

    static Tick create(Tick... fns) {
      Tick retval = (Tick) UnaryOperator.<NesPpu>identity();
      for (Tick fn : fns) {
        retval = (Tick) retval.andThen(fn);
      }
      return retval;
    }
  }

  private static final int NUM_SCANLINES = 261;
  private static final int NUM_PIXELS = 341;

  /*
      if (1 <= pixel && pixel <= 256) {
      if (pixel == 1 && isPreRender) {
        // clear vblank and sprite0 hit
        status = (byte) (status & 0b0011_1111);
        nmi = false;
      }
      if (!isPreRender) {
        renderPixel();
      }
      shiftRegisters();
      // Fetch tile data for this scanline
      fetchTileData((pixel - 1) % 8);
      if (pixel % 8 == 0) {
        incrementHorizontal();
        reloadShiftRegisters();
      }
      if (pixel == 256) {
        incrementVertical();
      }
      return;
    }
  */

  private static final Tick[] VISIBLE_SCANLINE = {
    /*   0 */ NesPpu::idle,
    /*   1 */ Tick.create(
        NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1),
    /*   2 */ Tick.create(
        NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2),
    /*   3 */ NesPpu::fetchAttributeByte1,
    /*   4 */ NesPpu::fetchAttributeByte2,
    /*   5 */ NesPpu::fetchPatternByteLo1,
    /*   6 */ NesPpu::fetchPatternByteLo2,
    /*   7 */ NesPpu::fetchPatternByteHi1,
    /*   8 */ NesPpu::fetchPatternByteHi2,
    /*   9 */ NesPpu::idle,
    /*  10 */ NesPpu::idle,
    /*  11 */ NesPpu::idle,
    /*  12 */ NesPpu::idle,
    /*  13 */ NesPpu::idle,
    /*  14 */ NesPpu::idle,
    /*  15 */ NesPpu::idle,
    /*  16 */ NesPpu::idle,
    /*  17 */ NesPpu::idle,
    /*  18 */ NesPpu::idle,
    /*  19 */ NesPpu::idle,
    /*  20 */ NesPpu::idle,
    /*  21 */ NesPpu::idle,
    /*  22 */ NesPpu::idle,
    /*  23 */ NesPpu::idle,
    /*  24 */ NesPpu::idle,
    /*  25 */ NesPpu::idle,
    /*  26 */ NesPpu::idle,
    /*  27 */ NesPpu::idle,
    /*  28 */ NesPpu::idle,
    /*  29 */ NesPpu::idle,
    /*  30 */ NesPpu::idle,
    /*  31 */ NesPpu::idle,
    /*  32 */ NesPpu::idle,
    /*  33 */ NesPpu::idle,
    /*  34 */ NesPpu::idle,
    /*  35 */ NesPpu::idle,
    /*  36 */ NesPpu::idle,
    /*  37 */ NesPpu::idle,
    /*  38 */ NesPpu::idle,
    /*  39 */ NesPpu::idle,
    /*  40 */ NesPpu::idle,
    /*  41 */ NesPpu::idle,
    /*  42 */ NesPpu::idle,
    /*  43 */ NesPpu::idle,
    /*  44 */ NesPpu::idle,
    /*  45 */ NesPpu::idle,
    /*  46 */ NesPpu::idle,
    /*  47 */ NesPpu::idle,
    /*  48 */ NesPpu::idle,
    /*  49 */ NesPpu::idle,
    /*  50 */ NesPpu::idle,
    /*  51 */ NesPpu::idle,
    /*  52 */ NesPpu::idle,
    /*  53 */ NesPpu::idle,
    /*  54 */ NesPpu::idle,
    /*  55 */ NesPpu::idle,
    /*  56 */ NesPpu::idle,
    /*  57 */ NesPpu::idle,
    /*  58 */ NesPpu::idle,
    /*  59 */ NesPpu::idle,
    /*  60 */ NesPpu::idle,
    /*  61 */ NesPpu::idle,
    /*  62 */ NesPpu::idle,
    /*  63 */ NesPpu::idle,
    /*  64 */ NesPpu::idle,
    /*  65 */ NesPpu::idle,
    /*  66 */ NesPpu::idle,
    /*  67 */ NesPpu::idle,
    /*  68 */ NesPpu::idle,
    /*  69 */ NesPpu::idle,
    /*  70 */ NesPpu::idle,
    /*  71 */ NesPpu::idle,
    /*  72 */ NesPpu::idle,
    /*  73 */ NesPpu::idle,
    /*  74 */ NesPpu::idle,
    /*  75 */ NesPpu::idle,
    /*  76 */ NesPpu::idle,
    /*  77 */ NesPpu::idle,
    /*  78 */ NesPpu::idle,
    /*  79 */ NesPpu::idle,
    /*  80 */ NesPpu::idle,
    /*  81 */ NesPpu::idle,
    /*  82 */ NesPpu::idle,
    /*  83 */ NesPpu::idle,
    /*  84 */ NesPpu::idle,
    /*  85 */ NesPpu::idle,
    /*  86 */ NesPpu::idle,
    /*  87 */ NesPpu::idle,
    /*  88 */ NesPpu::idle,
    /*  89 */ NesPpu::idle,
    /*  90 */ NesPpu::idle,
    /*  91 */ NesPpu::idle,
    /*  92 */ NesPpu::idle,
    /*  93 */ NesPpu::idle,
    /*  94 */ NesPpu::idle,
    /*  95 */ NesPpu::idle,
    /*  96 */ NesPpu::idle,
    /*  97 */ NesPpu::idle,
    /*  98 */ NesPpu::idle,
    /*  99 */ NesPpu::idle,
    /* 100 */ NesPpu::idle,
    /* 101 */ NesPpu::idle,
    /* 102 */ NesPpu::idle,
    /* 103 */ NesPpu::idle,
    /* 104 */ NesPpu::idle,
    /* 105 */ NesPpu::idle,
    /* 106 */ NesPpu::idle,
    /* 107 */ NesPpu::idle,
    /* 108 */ NesPpu::idle,
    /* 109 */ NesPpu::idle,
    /* 110 */ NesPpu::idle,
    /* 111 */ NesPpu::idle,
    /* 112 */ NesPpu::idle,
    /* 113 */ NesPpu::idle,
    /* 114 */ NesPpu::idle,
    /* 115 */ NesPpu::idle,
    /* 116 */ NesPpu::idle,
    /* 117 */ NesPpu::idle,
    /* 118 */ NesPpu::idle,
    /* 119 */ NesPpu::idle,
    /* 120 */ NesPpu::idle,
    /* 121 */ NesPpu::idle,
    /* 122 */ NesPpu::idle,
    /* 123 */ NesPpu::idle,
    /* 124 */ NesPpu::idle,
    /* 125 */ NesPpu::idle,
    /* 126 */ NesPpu::idle,
    /* 127 */ NesPpu::idle,
    /* 128 */ NesPpu::idle,
    /* 129 */ NesPpu::idle,
    /* 130 */ NesPpu::idle,
    /* 131 */ NesPpu::idle,
    /* 132 */ NesPpu::idle,
    /* 133 */ NesPpu::idle,
    /* 134 */ NesPpu::idle,
    /* 135 */ NesPpu::idle,
    /* 136 */ NesPpu::idle,
    /* 137 */ NesPpu::idle,
    /* 138 */ NesPpu::idle,
    /* 139 */ NesPpu::idle,
    /* 140 */ NesPpu::idle,
    /* 141 */ NesPpu::idle,
    /* 142 */ NesPpu::idle,
    /* 143 */ NesPpu::idle,
    /* 144 */ NesPpu::idle,
    /* 145 */ NesPpu::idle,
    /* 146 */ NesPpu::idle,
    /* 147 */ NesPpu::idle,
    /* 148 */ NesPpu::idle,
    /* 149 */ NesPpu::idle,
    /* 150 */ NesPpu::idle,
    /* 151 */ NesPpu::idle,
    /* 152 */ NesPpu::idle,
    /* 153 */ NesPpu::idle,
    /* 154 */ NesPpu::idle,
    /* 155 */ NesPpu::idle,
    /* 156 */ NesPpu::idle,
    /* 157 */ NesPpu::idle,
    /* 158 */ NesPpu::idle,
    /* 159 */ NesPpu::idle,
    /* 160 */ NesPpu::idle,
    /* 161 */ NesPpu::idle,
    /* 162 */ NesPpu::idle,
    /* 163 */ NesPpu::idle,
    /* 164 */ NesPpu::idle,
    /* 165 */ NesPpu::idle,
    /* 166 */ NesPpu::idle,
    /* 167 */ NesPpu::idle,
    /* 168 */ NesPpu::idle,
    /* 169 */ NesPpu::idle,
    /* 170 */ NesPpu::idle,
    /* 171 */ NesPpu::idle,
    /* 172 */ NesPpu::idle,
    /* 173 */ NesPpu::idle,
    /* 174 */ NesPpu::idle,
    /* 175 */ NesPpu::idle,
    /* 176 */ NesPpu::idle,
    /* 177 */ NesPpu::idle,
    /* 178 */ NesPpu::idle,
    /* 179 */ NesPpu::idle,
    /* 180 */ NesPpu::idle,
    /* 181 */ NesPpu::idle,
    /* 182 */ NesPpu::idle,
    /* 183 */ NesPpu::idle,
    /* 184 */ NesPpu::idle,
    /* 185 */ NesPpu::idle,
    /* 186 */ NesPpu::idle,
    /* 187 */ NesPpu::idle,
    /* 188 */ NesPpu::idle,
    /* 189 */ NesPpu::idle,
    /* 190 */ NesPpu::idle,
    /* 191 */ NesPpu::idle,
    /* 192 */ NesPpu::idle,
    /* 193 */ NesPpu::idle,
    /* 194 */ NesPpu::idle,
    /* 195 */ NesPpu::idle,
    /* 196 */ NesPpu::idle,
    /* 197 */ NesPpu::idle,
    /* 198 */ NesPpu::idle,
    /* 199 */ NesPpu::idle,
    /* 200 */ NesPpu::idle,
    /* 201 */ NesPpu::idle,
    /* 202 */ NesPpu::idle,
    /* 203 */ NesPpu::idle,
    /* 204 */ NesPpu::idle,
    /* 205 */ NesPpu::idle,
    /* 206 */ NesPpu::idle,
    /* 207 */ NesPpu::idle,
    /* 208 */ NesPpu::idle,
    /* 209 */ NesPpu::idle,
    /* 210 */ NesPpu::idle,
    /* 211 */ NesPpu::idle,
    /* 212 */ NesPpu::idle,
    /* 213 */ NesPpu::idle,
    /* 214 */ NesPpu::idle,
    /* 215 */ NesPpu::idle,
    /* 216 */ NesPpu::idle,
    /* 217 */ NesPpu::idle,
    /* 218 */ NesPpu::idle,
    /* 219 */ NesPpu::idle,
    /* 220 */ NesPpu::idle,
    /* 221 */ NesPpu::idle,
    /* 222 */ NesPpu::idle,
    /* 223 */ NesPpu::idle,
    /* 224 */ NesPpu::idle,
    /* 225 */ NesPpu::idle,
    /* 226 */ NesPpu::idle,
    /* 227 */ NesPpu::idle,
    /* 228 */ NesPpu::idle,
    /* 229 */ NesPpu::idle,
    /* 230 */ NesPpu::idle,
    /* 231 */ NesPpu::idle,
    /* 232 */ NesPpu::idle,
    /* 233 */ NesPpu::idle,
    /* 234 */ NesPpu::idle,
    /* 235 */ NesPpu::idle,
    /* 236 */ NesPpu::idle,
    /* 237 */ NesPpu::idle,
    /* 238 */ NesPpu::idle,
    /* 239 */ NesPpu::idle,
    /* 240 */ NesPpu::idle,
    /* 241 */ NesPpu::idle,
    /* 242 */ NesPpu::idle,
    /* 243 */ NesPpu::idle,
    /* 244 */ NesPpu::idle,
    /* 245 */ NesPpu::idle,
    /* 246 */ NesPpu::idle,
    /* 247 */ NesPpu::idle,
    /* 248 */ NesPpu::idle,
    /* 249 */ NesPpu::idle,
    /* 250 */ NesPpu::idle,
    /* 251 */ NesPpu::idle,
    /* 252 */ NesPpu::idle,
    /* 253 */ NesPpu::idle,
    /* 254 */ NesPpu::idle,
    /* 255 */ NesPpu::idle,
    /* 256 */ NesPpu::idle,
    /* 257 */ NesPpu::idle,
    /* 258 */ NesPpu::idle,
    /* 259 */ NesPpu::idle,
    /* 260 */ NesPpu::idle,
    /* 261 */ NesPpu::idle,
    /* 262 */ NesPpu::idle,
    /* 263 */ NesPpu::idle,
    /* 264 */ NesPpu::idle,
    /* 265 */ NesPpu::idle,
    /* 266 */ NesPpu::idle,
    /* 267 */ NesPpu::idle,
    /* 268 */ NesPpu::idle,
    /* 269 */ NesPpu::idle,
    /* 270 */ NesPpu::idle,
    /* 271 */ NesPpu::idle,
    /* 272 */ NesPpu::idle,
    /* 273 */ NesPpu::idle,
    /* 274 */ NesPpu::idle,
    /* 275 */ NesPpu::idle,
    /* 276 */ NesPpu::idle,
    /* 277 */ NesPpu::idle,
    /* 278 */ NesPpu::idle,
    /* 279 */ NesPpu::idle,
    /* 280 */ NesPpu::idle,
    /* 281 */ NesPpu::idle,
    /* 282 */ NesPpu::idle,
    /* 283 */ NesPpu::idle,
    /* 284 */ NesPpu::idle,
    /* 285 */ NesPpu::idle,
    /* 286 */ NesPpu::idle,
    /* 287 */ NesPpu::idle,
    /* 288 */ NesPpu::idle,
    /* 289 */ NesPpu::idle,
    /* 290 */ NesPpu::idle,
    /* 291 */ NesPpu::idle,
    /* 292 */ NesPpu::idle,
    /* 293 */ NesPpu::idle,
    /* 294 */ NesPpu::idle,
    /* 295 */ NesPpu::idle,
    /* 296 */ NesPpu::idle,
    /* 297 */ NesPpu::idle,
    /* 298 */ NesPpu::idle,
    /* 299 */ NesPpu::idle,
    /* 300 */ NesPpu::idle,
    /* 301 */ NesPpu::idle,
    /* 302 */ NesPpu::idle,
    /* 303 */ NesPpu::idle,
    /* 304 */ NesPpu::idle,
    /* 305 */ NesPpu::idle,
    /* 306 */ NesPpu::idle,
    /* 307 */ NesPpu::idle,
    /* 308 */ NesPpu::idle,
    /* 309 */ NesPpu::idle,
    /* 310 */ NesPpu::idle,
    /* 311 */ NesPpu::idle,
    /* 312 */ NesPpu::idle,
    /* 313 */ NesPpu::idle,
    /* 314 */ NesPpu::idle,
    /* 315 */ NesPpu::idle,
    /* 316 */ NesPpu::idle,
    /* 317 */ NesPpu::idle,
    /* 318 */ NesPpu::idle,
    /* 319 */ NesPpu::idle,
    /* 320 */ NesPpu::idle,
    /* 321 */ NesPpu::idle,
    /* 322 */ NesPpu::idle,
    /* 323 */ NesPpu::idle,
    /* 324 */ NesPpu::idle,
    /* 325 */ NesPpu::idle,
    /* 326 */ NesPpu::idle,
    /* 327 */ NesPpu::idle,
    /* 328 */ NesPpu::idle,
    /* 329 */ NesPpu::idle,
    /* 330 */ NesPpu::idle,
    /* 331 */ NesPpu::idle,
    /* 332 */ NesPpu::idle,
    /* 333 */ NesPpu::idle,
    /* 334 */ NesPpu::idle,
    /* 335 */ NesPpu::idle,
    /* 336 */ NesPpu::idle,
    /* 337 */ NesPpu::idle,
    /* 338 */ NesPpu::idle,
    /* 339 */ NesPpu::idle,
    /* 340 */ NesPpu::idle,
  };

  private static final Tick[][] SCANLINES = {
    /*   0 */ new Tick[] {},
    /*   1 */ new Tick[] {},
    /*   2 */ new Tick[] {},
    /*   3 */ new Tick[] {},
    /*   4 */ new Tick[] {},
    /*   5 */ new Tick[] {},
    /*   6 */ new Tick[] {},
    /*   7 */ new Tick[] {},
    /*   8 */ new Tick[] {},
    /*   9 */ new Tick[] {},
    /*  10 */ new Tick[] {},
    /*  11 */ new Tick[] {},
    /*  12 */ new Tick[] {},
    /*  13 */ new Tick[] {},
    /*  14 */ new Tick[] {},
    /*  15 */ new Tick[] {},
    /*  16 */ new Tick[] {},
    /*  17 */ new Tick[] {},
    /*  18 */ new Tick[] {},
    /*  19 */ new Tick[] {},
    /*  20 */ new Tick[] {},
    /*  21 */ new Tick[] {},
    /*  22 */ new Tick[] {},
    /*  23 */ new Tick[] {},
    /*  24 */ new Tick[] {},
    /*  25 */ new Tick[] {},
    /*  26 */ new Tick[] {},
    /*  27 */ new Tick[] {},
    /*  28 */ new Tick[] {},
    /*  29 */ new Tick[] {},
    /*  30 */ new Tick[] {},
    /*  31 */ new Tick[] {},
    /*  32 */ new Tick[] {},
    /*  33 */ new Tick[] {},
    /*  34 */ new Tick[] {},
    /*  35 */ new Tick[] {},
    /*  36 */ new Tick[] {},
    /*  37 */ new Tick[] {},
    /*  38 */ new Tick[] {},
    /*  39 */ new Tick[] {},
    /*  40 */ new Tick[] {},
    /*  41 */ new Tick[] {},
    /*  42 */ new Tick[] {},
    /*  43 */ new Tick[] {},
    /*  44 */ new Tick[] {},
    /*  45 */ new Tick[] {},
    /*  46 */ new Tick[] {},
    /*  47 */ new Tick[] {},
    /*  48 */ new Tick[] {},
    /*  49 */ new Tick[] {},
    /*  50 */ new Tick[] {},
    /*  51 */ new Tick[] {},
    /*  52 */ new Tick[] {},
    /*  53 */ new Tick[] {},
    /*  54 */ new Tick[] {},
    /*  55 */ new Tick[] {},
    /*  56 */ new Tick[] {},
    /*  57 */ new Tick[] {},
    /*  58 */ new Tick[] {},
    /*  59 */ new Tick[] {},
    /*  60 */ new Tick[] {},
    /*  61 */ new Tick[] {},
    /*  62 */ new Tick[] {},
    /*  63 */ new Tick[] {},
    /*  64 */ new Tick[] {},
    /*  65 */ new Tick[] {},
    /*  66 */ new Tick[] {},
    /*  67 */ new Tick[] {},
    /*  68 */ new Tick[] {},
    /*  69 */ new Tick[] {},
    /*  70 */ new Tick[] {},
    /*  71 */ new Tick[] {},
    /*  72 */ new Tick[] {},
    /*  73 */ new Tick[] {},
    /*  74 */ new Tick[] {},
    /*  75 */ new Tick[] {},
    /*  76 */ new Tick[] {},
    /*  77 */ new Tick[] {},
    /*  78 */ new Tick[] {},
    /*  79 */ new Tick[] {},
    /*  80 */ new Tick[] {},
    /*  81 */ new Tick[] {},
    /*  82 */ new Tick[] {},
    /*  83 */ new Tick[] {},
    /*  84 */ new Tick[] {},
    /*  85 */ new Tick[] {},
    /*  86 */ new Tick[] {},
    /*  87 */ new Tick[] {},
    /*  88 */ new Tick[] {},
    /*  89 */ new Tick[] {},
    /*  90 */ new Tick[] {},
    /*  91 */ new Tick[] {},
    /*  92 */ new Tick[] {},
    /*  93 */ new Tick[] {},
    /*  94 */ new Tick[] {},
    /*  95 */ new Tick[] {},
    /*  96 */ new Tick[] {},
    /*  97 */ new Tick[] {},
    /*  98 */ new Tick[] {},
    /*  99 */ new Tick[] {},
    /* 100 */ new Tick[] {},
    /* 101 */ new Tick[] {},
    /* 102 */ new Tick[] {},
    /* 103 */ new Tick[] {},
    /* 104 */ new Tick[] {},
    /* 105 */ new Tick[] {},
    /* 106 */ new Tick[] {},
    /* 107 */ new Tick[] {},
    /* 108 */ new Tick[] {},
    /* 109 */ new Tick[] {},
    /* 110 */ new Tick[] {},
    /* 111 */ new Tick[] {},
    /* 112 */ new Tick[] {},
    /* 113 */ new Tick[] {},
    /* 114 */ new Tick[] {},
    /* 115 */ new Tick[] {},
    /* 116 */ new Tick[] {},
    /* 117 */ new Tick[] {},
    /* 118 */ new Tick[] {},
    /* 119 */ new Tick[] {},
    /* 120 */ new Tick[] {},
    /* 121 */ new Tick[] {},
    /* 122 */ new Tick[] {},
    /* 123 */ new Tick[] {},
    /* 124 */ new Tick[] {},
    /* 125 */ new Tick[] {},
    /* 126 */ new Tick[] {},
    /* 127 */ new Tick[] {},
    /* 128 */ new Tick[] {},
    /* 129 */ new Tick[] {},
    /* 130 */ new Tick[] {},
    /* 131 */ new Tick[] {},
    /* 132 */ new Tick[] {},
    /* 133 */ new Tick[] {},
    /* 134 */ new Tick[] {},
    /* 135 */ new Tick[] {},
    /* 136 */ new Tick[] {},
    /* 137 */ new Tick[] {},
    /* 138 */ new Tick[] {},
    /* 139 */ new Tick[] {},
    /* 140 */ new Tick[] {},
    /* 141 */ new Tick[] {},
    /* 142 */ new Tick[] {},
    /* 143 */ new Tick[] {},
    /* 144 */ new Tick[] {},
    /* 145 */ new Tick[] {},
    /* 146 */ new Tick[] {},
    /* 147 */ new Tick[] {},
    /* 148 */ new Tick[] {},
    /* 149 */ new Tick[] {},
    /* 150 */ new Tick[] {},
    /* 151 */ new Tick[] {},
    /* 152 */ new Tick[] {},
    /* 153 */ new Tick[] {},
    /* 154 */ new Tick[] {},
    /* 155 */ new Tick[] {},
    /* 156 */ new Tick[] {},
    /* 157 */ new Tick[] {},
    /* 158 */ new Tick[] {},
    /* 159 */ new Tick[] {},
    /* 160 */ new Tick[] {},
    /* 161 */ new Tick[] {},
    /* 162 */ new Tick[] {},
    /* 163 */ new Tick[] {},
    /* 164 */ new Tick[] {},
    /* 165 */ new Tick[] {},
    /* 166 */ new Tick[] {},
    /* 167 */ new Tick[] {},
    /* 168 */ new Tick[] {},
    /* 169 */ new Tick[] {},
    /* 170 */ new Tick[] {},
    /* 171 */ new Tick[] {},
    /* 172 */ new Tick[] {},
    /* 173 */ new Tick[] {},
    /* 174 */ new Tick[] {},
    /* 175 */ new Tick[] {},
    /* 176 */ new Tick[] {},
    /* 177 */ new Tick[] {},
    /* 178 */ new Tick[] {},
    /* 179 */ new Tick[] {},
    /* 180 */ new Tick[] {},
    /* 181 */ new Tick[] {},
    /* 182 */ new Tick[] {},
    /* 183 */ new Tick[] {},
    /* 184 */ new Tick[] {},
    /* 185 */ new Tick[] {},
    /* 186 */ new Tick[] {},
    /* 187 */ new Tick[] {},
    /* 188 */ new Tick[] {},
    /* 189 */ new Tick[] {},
    /* 190 */ new Tick[] {},
    /* 191 */ new Tick[] {},
    /* 192 */ new Tick[] {},
    /* 193 */ new Tick[] {},
    /* 194 */ new Tick[] {},
    /* 195 */ new Tick[] {},
    /* 196 */ new Tick[] {},
    /* 197 */ new Tick[] {},
    /* 198 */ new Tick[] {},
    /* 199 */ new Tick[] {},
    /* 200 */ new Tick[] {},
    /* 201 */ new Tick[] {},
    /* 202 */ new Tick[] {},
    /* 203 */ new Tick[] {},
    /* 204 */ new Tick[] {},
    /* 205 */ new Tick[] {},
    /* 206 */ new Tick[] {},
    /* 207 */ new Tick[] {},
    /* 208 */ new Tick[] {},
    /* 209 */ new Tick[] {},
    /* 210 */ new Tick[] {},
    /* 211 */ new Tick[] {},
    /* 212 */ new Tick[] {},
    /* 213 */ new Tick[] {},
    /* 214 */ new Tick[] {},
    /* 215 */ new Tick[] {},
    /* 216 */ new Tick[] {},
    /* 217 */ new Tick[] {},
    /* 218 */ new Tick[] {},
    /* 219 */ new Tick[] {},
    /* 220 */ new Tick[] {},
    /* 221 */ new Tick[] {},
    /* 222 */ new Tick[] {},
    /* 223 */ new Tick[] {},
    /* 224 */ new Tick[] {},
    /* 225 */ new Tick[] {},
    /* 226 */ new Tick[] {},
    /* 227 */ new Tick[] {},
    /* 228 */ new Tick[] {},
    /* 229 */ new Tick[] {},
    /* 230 */ new Tick[] {},
    /* 231 */ new Tick[] {},
    /* 232 */ new Tick[] {},
    /* 233 */ new Tick[] {},
    /* 234 */ new Tick[] {},
    /* 235 */ new Tick[] {},
    /* 236 */ new Tick[] {},
    /* 237 */ new Tick[] {},
    /* 238 */ new Tick[] {},
    /* 239 */ new Tick[] {},
    /* 240 */ new Tick[] {},
    /* 241 */ new Tick[] {},
    /* 242 */ new Tick[] {},
    /* 243 */ new Tick[] {},
    /* 244 */ new Tick[] {},
    /* 245 */ new Tick[] {},
    /* 246 */ new Tick[] {},
    /* 247 */ new Tick[] {},
    /* 248 */ new Tick[] {},
    /* 249 */ new Tick[] {},
    /* 250 */ new Tick[] {},
    /* 251 */ new Tick[] {},
    /* 252 */ new Tick[] {},
    /* 253 */ new Tick[] {},
    /* 254 */ new Tick[] {},
    /* 255 */ new Tick[] {},
    /* 256 */ new Tick[] {},
    /* 257 */ new Tick[] {},
    /* 258 */ new Tick[] {},
    /* 259 */ new Tick[] {},
    /* 260 */ new Tick[] {},
  };

  private final NesMapper mapper;
  private final DrawFrame drawFrame;
  private final byte[] ram;
  private final byte[] oam;
  private final byte[] palette;
  private final byte[] frame = new byte[256 * 240 * 3];

  private int scanline = 0;
  private int pixel = 0;
  private byte buffer = 0;
  private boolean nmi = false;

  private byte control = 0;
  private byte mask = 0;
  private byte status = 0;
  private byte oamAddress = 0;

  private boolean odd = false;
  private short v = 0;
  private short t = 0;
  private byte x = 0;
  private byte w = 0;

  private byte nametableByte;
  private byte attributeByte;
  private byte patternLoLatch;
  private byte patternHiLatch;
  private short patternLoShift;
  private short patternHiShift;
  private short attributeLoShift;
  private short attributeHiShift;

  public NesPpu(NesMapper mapper, DrawFrame drawFrame) {
    this.mapper = mapper;
    this.drawFrame = drawFrame;
    this.ram = new byte[0x2000];
    this.oam = new byte[0x0100];
    this.palette = new byte[0x0100];
  }

  public void writeControl(byte data) {
    boolean prev_generate_nmi = bit8(control, 7) == 1;

    control = data;

    boolean generate_nmi = bit8(control, 7) == 1;
    boolean in_vblank = bit8(status, 7) == 1;
    if (!prev_generate_nmi && generate_nmi && in_vblank) {
      nmi = true;
    }

    /*
    t: ...GH.. ........ <- d: ......GH
       <used elsewhere> <- d: ABCDEF..
    */
    t = (short) ((t & 0b0111_0011_1111_1111) | ((Byte.toUnsignedInt(data) & 0b0000_0011) << 10));
  }

  public byte readMask() {
    return mask;
  }

  public void writeMask(byte data) {
    mask = data;
  }

  public byte readStatus() {
    byte result = status;
    status &= 0b0111_1111;
    w = 0;
    return result;
  }

  public void writeOamAddr(byte data) {
    oamAddress = data;
  }

  public byte readOamData() {
    return oam[Byte.toUnsignedInt(oamAddress)];
  }

  public void writeOamData(byte data) {
    oam[Byte.toUnsignedInt(oamAddress)] = data;
    incrementOamAddress();
  }

  public void writeOamDma(byte[] buffer) {
    for (byte b : buffer) {
      oam[Byte.toUnsignedInt(oamAddress)] = b;
      incrementOamAddress();
    }
  }

  public void writeScroll(byte data) {
    if (w == 0) {
      /*
      t: ....... ...ABCDE <- d: ABCDE...
      x:              FGH <- d: .....FGH
      w:                  <- 1
      */
      t =
          (short)
              ((t & 0b0111_1111_1110_0000)
                  | (byte) ((Byte.toUnsignedInt(data) & 0b0001_1111) >>> 3));
      x = (byte) (Byte.toUnsignedInt(data) & 0b0000_0111);
      w = 1;
    } else {
      /*
      t: FGH..AB CDE..... <- d: ABCDEFGH
      w:                  <- 0
      */
      t =
          (short)
              ((t & 0b0000_1100_0001_1111)
                  | ((Byte.toUnsignedInt(data) & 0b1111_1000) << 2)
                  | ((Byte.toUnsignedInt(data) & 0b0000_0111) << 12));
      w = 0;
    }
  }

  public void writeAddress(byte data) {
    if (w == 0) {
      /*
      t: .CDEFGH ........ <- d: ..CDEFGH
             <unused>     <- d: AB......
      t: Z...... ........ <- 0 (bit Z is cleared)
      w:                  <- 1
      */
      t =
          (short)
              ((t & 0b0000_0000_1111_1111)
                  | (short) ((Byte.toUnsignedInt(data) << 8) & 0b0011_1111_0000_0000));
      w = 1;
    } else {
      /*
      t: ....... ABCDEFGH <- d: ABCDEFGH
      v: <...all bits...> <- t: <...all bits...>
      w:                  <- 0
      */
      t = (short) ((t & 0b0111_1111_0000_0000) | Byte.toUnsignedInt(data));
      v = t;
      w = 0;
    }
  }

  public byte readData() {
    byte result;
    if (0 <= v && v < 0x2000) {
      result = buffer;
      buffer = mapper.readChr(v);
      incrementAddress();
      return result;
    }
    if (0x2000 <= v && v < 0x3000) {
      result = buffer;
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    if (0x3000 <= v && v < 0x3f00) {
      result = buffer;
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    if (0x3f00 <= v && v < 0x4000) {
      result = palette[getPaletteAddress(v)];
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    throw new IllegalArgumentException("cannot read from PPU address " + v);
  }

  public void writeData(byte data) {
    if (0 <= v && v < 0x2000) {
      mapper.writeChr(v, data);
      incrementAddress();
      return;
    }
    if (0x2000 <= v && v < 0x3000) {
      ram[mapper.getNametableMirrorAddress(v)] = data;
      incrementAddress();
      return;
    }
    if (0x3000 <= v && v < 0x3f00) {
      ram[mapper.getNametableMirrorAddress(v)] = data;
      incrementAddress();
      return;
    }
    if (0x3f00 <= v && v < 0x4000) {
      palette[getPaletteAddress(v)] = data;
      incrementAddress();
      return;
    }
    throw new IllegalArgumentException("cannot write to PPU address " + v);
  }

  private int getPaletteAddress(int address) {
    if (address == 0x3f10 || address == 0x3f14 || address == 0x3f18 || address == 0x3f1c) {
      return address - 0x0010 - 0x3f00;
    }
    return address - 0x3f00;
  }

  private void incrementAddress() {
    if (bit8(control, 2) == 1) {
      v += 32;
    } else {
      v += 1;
    }
  }

  // https://www.nesdev.org/wiki/PPU_scrolling#Wrapping_around
  private void incrementHorizontal() {
    int coarseX = (v & 0b0000_0000_0001_1111) >>> 0;

    coarseX = (coarseX + 1) % 32;

    if (coarseX == 0) {
      // Switch horizontal nametable
      v ^= 0b0000_0100_0000_0000;
    }

    v &= 0b0111_1111_1110_0000;
    v |= (short) (coarseX << 0);
  }

  private void incrementVertical() {
    int fineY = (v & 0b0111_0000_0000_0000) >>> 12;
    int coarseY = (v & 0b0000_0011_1110_0000) >>> 5;

    fineY = (fineY + 1) % 8;

    if (fineY == 0) {
      coarseY++;
      if (coarseY == 30) {
        // Coarse Y wraps around.  Set it to 0 and switch vertical nametable.
        coarseY = 0;
        v ^= 0b0000_1000_0000_0000;
      } else if (coarseY == 32) {
        // Coarse Y wraps around, BUT don't switch the vertical nametable.
        coarseY = 0;
      }
    }

    v &= 0b0000_1100_0001_1111;
    v |= (short) (fineY << 12);
    v |= (short) (coarseY << 5);
  }

  private void incrementOamAddress() {
    oamAddress = (byte) ((oamAddress + 1) % oam.length);
  }

  public void tick() {
    if (isRenderingEnabled()) {
      if (0 <= scanline && scanline <= 239) {
        doVisibleScanline(false);
      }
      if (scanline == 240) {
        doPostRenderScanline();
        if (pixel == 0) {
          drawFrame.draw(frame);
        }
      }
    }
    if (241 <= scanline && scanline <= 260) {
      doVerticalBlankingScanline();
    }
    if (scanline == 261) {
      doVisibleScanline(true);
    }
    if (scanline == 261) {
      if ((pixel == 339 && odd) || pixel == 340) {
        scanline = 0;
        pixel = 0;
        odd = !odd;
      } else {
        pixel++;
      }
    } else {
      if (pixel == 340) {
        scanline++;
        pixel = 0;
      } else {
        pixel++;
      }
    }
  }

  private void doVisibleScanline(boolean isPreRender) {
    if (pixel == 0) {
      idle();
      return;
    }
    if (1 <= pixel && pixel <= 256) {
      if (pixel == 1 && isPreRender) {
        // clear vblank and sprite0 hit
        status = (byte) (status & 0b0011_1111);
        nmi = false;
      }
      if (!isPreRender) {
        renderPixel();
      }
      shiftRegisters();
      // Fetch tile data for this scanline
      fetchTileData((pixel - 1) % 8);
      if (pixel % 8 == 0) {
        incrementHorizontal();
        reloadShiftRegisters();
      }
      if (pixel == 256) {
        incrementVertical();
      }
      return;
    }
    if (257 <= pixel && pixel <= 320) {
      if (pixel == 257) {
        // horiz(v) = horiz(t)
        // v: ....A.. ...BCDEF <- t: ....A.. ...BCDEF
        v &= 0b0111_1011_1110_0000;
        v |= (short) (t & 0b0000_0100_0001_1111);
      }
      if (isPreRender && 280 <= pixel && pixel <= 304) {
        // vert(v) = vert(t)
        // v: GHIA.BC DEF..... <- t: GHIA.BC DEF.....
        v &= 0b0000_0100_0001_1111;
        v |= (short) (t & 0b0111_1011_1110_0000);
      }
      // Fetch sprite data for the *next* scanline
      fetchSpriteData((pixel - 1) % 8);
      return;
    }
    if (321 <= pixel && pixel <= 336) {
      shiftRegisters();
      // Fetch the first two tiles for the *next* scanline
      fetchTileData((pixel - 1) % 8);
      if (pixel % 8 == 0) {
        incrementHorizontal();
        reloadShiftRegisters();
      }
      return;
    }
    if (337 <= pixel && pixel <= 340) {
      // Fetch unused bytes
      fetchUnusedBytes((pixel - 1) % 4);
      return;
    }
    // TODO: sprite evaluation for the next scanline
    throw new IllegalStateException();
  }

  private NesPpu renderPixel() {
    int patternHi = bit16(patternHiShift, 15 - x);
    int patternLo = bit16(patternLoShift, 15 - x);
    int attributeHi = bit16(attributeHiShift, 15 - x);
    int attributeLo = bit16(attributeLoShift, 15 - x);
    NesPpuColor[] colors = getBackgroundPalette((attributeHi << 1) | attributeLo);
    NesPpuColor color =
        switch ((patternHi << 1) | patternLo) {
          case 0 -> colors[0];
          case 1 -> colors[1];
          case 2 -> colors[2];
          case 3 -> colors[3];
          default -> throw new IllegalStateException();
        };
    int i = 3 * (scanline * 256 + pixel - 1);
    frame[i + 0] = color.r();
    frame[i + 1] = color.g();
    frame[i + 2] = color.b();
    return this;
  }

  private NesPpu shiftRegisters() {
    patternLoShift <<= 1;
    patternHiShift <<= 1;
    attributeLoShift <<= 1;
    attributeHiShift <<= 1;
    return this;
  }

  private NesPpu idle() {
    return this;
  }

  private NesPpu fetchNametableByte1() {
    return this;
  }

  private NesPpu fetchNametableByte2() {
    nametableByte = fetchNametableByte();
    return this;
  }

  private void fetchTileData(int phase) {
    if (phase == 0) {
      // start fetch of nametable byte
      return;
    }
    if (phase == 1) {
      // finish fetch of nametable byte
      nametableByte = fetchNametableByte();
      return;
    }
    if (phase == 2) {
      // start fetch of attribute table byte
      return;
    }
    if (phase == 3) {
      // finish fetch of attribute table byte
      attributeByte = fetchAttributeByte();
      return;
    }
    if (phase == 4) {
      // start fetch of pattern table tile low byte into latch
      return;
    }
    if (phase == 5) {
      // finish fetch of pattern table tile low byte into latch
      patternLoLatch = fetchPatternTableByte(0);
      return;
    }
    if (phase == 6) {
      // start fetch of pattern table tile high byte into latch
      return;
    }
    if (phase == 7) {
      // finish fetch of pattern table tile high byte into latch
      patternHiLatch = fetchPatternTableByte(8);
      return;
    }
    throw new IllegalStateException();
  }

  private void fetchSpriteData(int phase) {
    // TODO: fetch sprite data
  }

  private void fetchUnusedBytes(int phase) {
    if (phase == 0) {
      // start fetch of first unused byte
      return;
    }
    if (phase == 1) {
      // finish fetch of first unused byte
      fetchNametableByte();
      return;
    }
    if (phase == 2) {
      // start fetch of second unused byte
      return;
    }
    if (phase == 3) {
      // finish fetch of second unused byte
      fetchNametableByte();
      return;
    }
    throw new IllegalStateException();
  }

  private byte fetchNametableByte() {
    short address = (short) (0x2000 | (v & 0x0fff));
    //    return ram[mapper.getNametableMirrorAddress(address)];
    return ram[v & 0x0fff];
  }

  private byte fetchAttributeByte() {
    //    short mainNametable =
    //        switch (Byte.toUnsignedInt(control) & 0b0000_0011) {
    //          case 0 -> 0x0000;
    //          case 1 -> (short) (mapper.isVerticalMirroring() ? 0x0400 : 0x0000);
    //          case 2 -> (short) (mapper.isVerticalMirroring() ? 0x0000 : 0x0400);
    //          case 3 -> 0x0400;
    //          default -> throw new IllegalStateException();
    //        };
    //
    //    int tileRow = scanline / 8;
    //    int tileColumn = pixel / 8;
    //    byte attrByte = ram[nametable + 0x03c0 + tileRow / 4 * 8 + tileColumn / 4];

    int nametab = (v >> 0) & 0b0000_1100_0000_0000;
    int coarseY = (v >> 4) & 0b0000_0000_0011_1000;
    int coarseX = (v >> 2) & 0b0000_0000_0000_0111;
    int address = 0b0000_0011_1100_0000 | nametab | coarseY | coarseX;
    //    return ram[address % ram.length];
    return ram[address];
  }

  private byte fetchPatternTableByte(int offset) {
    int chrBank = bit8(control, 4) == 0 ? 0b0000_0000_0000_0000 : 0b0001_0000_0000_0000;
    int fineY = (v >> 12) & 0b0111;
    int chrIndex = chrBank | (Byte.toUnsignedInt(nametableByte) << 4) | offset | fineY;
    return mapper.readChr((short) chrIndex);
  }

  private void doPostRenderScanline() {
    idle();
  }

  private void doVerticalBlankingScanline() {
    if (pixel == 1 && scanline == 241) {
      // set vblank
      status = (byte) (status | 0b1000_0000);
      nmi = bit8(control, 7) == 1;
      return;
    }
    idle();
  }

  private void reloadShiftRegisters() {
    attributeLoShift &= (short) 0xff00;
    attributeLoShift |= (short) (bit8(attributeByte, 0) == 0 ? 0x0000 : 0x00ff);

    attributeHiShift &= (short) 0xff00;
    attributeHiShift |= (short) (bit8(attributeByte, 1) == 0 ? 0x0000 : 0x00ff);

    patternLoShift &= (short) 0xff00;
    patternLoShift |= (short) Byte.toUnsignedInt(patternLoLatch);

    patternHiShift &= (short) 0xff00;
    patternHiShift |= (short) Byte.toUnsignedInt(patternHiLatch);
  }

  private NesPpuColor[] getBackgroundPalette(int attribute) {
    return new NesPpuColor[] {
      NesPpuPalette.SYSTEM_PALETTE[palette[0]],
      NesPpuPalette.SYSTEM_PALETTE[palette[attribute * 4 + 1]],
      NesPpuPalette.SYSTEM_PALETTE[palette[attribute * 4 + 2]],
      NesPpuPalette.SYSTEM_PALETTE[palette[attribute * 4 + 3]],
    };
  }

  private boolean isRenderingEnabled() {
    return bit8(mask, 3) == 1 || bit8(mask, 4) == 1;
  }

  public boolean nmi() {
    boolean result = nmi;
    nmi = false;
    return result;
  }

  private static int bit8(byte value, int i) {
    return (Byte.toUnsignedInt(value) >>> i) & 1;
  }

  private int bit16(short value, int i) {
    return (Short.toUnsignedInt(value) >>> i) & 1;
  }
}

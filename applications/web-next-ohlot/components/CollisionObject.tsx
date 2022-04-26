/**
 * 충돌에 대해 인지해야할 것
 * 내 물체의 위치
 * 내 물체의 크기
 * 내 물체의 무게
 * 내 물체의 속력
 * 내 물체의 방향?
 *
 * 위의 내용과 동일하게 상대도 필요함
 * 흠 그렇다고 상대의 위치나 정보 등 상세하게 알 필요는 없을꺼같고
 * 결국 저 모든걸 계산해서 물체 좌표 범위엔 반발계수의 값이 남아야할 것.
 *
 */
import {useEffect, useState} from "react";

export default function CollisionObject() {
    const [isGrab, setGrab] = useState(false)
    const [oriX, setOriX] = useState(150.0)
    const [oriY, setOriY] = useState(50.0)

    return (

        <div
            onMouseDown={() => {
                setGrab(true)
            }}
            onMouseUp={() => {
                setGrab(false)
            }}
            onMouseMove={(event) => {
                if (isGrab) {
                    setOriX(event.clientX - 5)
                    setOriY(event.clientY - 5)
                }
            }}
            onMouseLeave={(event) => {
                console.log(event)
            }}
            className={"container"}>
                <div className={"children"}>
                    봄
                </div>
            <style jsx>
                {
                    `
                        .container {
                            position: absolute;
                            top: ${oriY}px;
                            left: ${oriX}px;
                            margin: 0 auto;
                            background-color: pink;
                            border: 1px solid black;
                            display: table;
                            border-radius 8x;
                        }
                        .children {
                            padding: 20px;
                        }
                    `
                }
            </style>
        </div>
    )
}
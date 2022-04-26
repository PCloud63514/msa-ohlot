import Link from 'next/link'
import {useRouter} from "next/router";

// React Router Dom과 비슷한 Link로, Anchor를 꼭 넣을 필요는 없다. inspect 해보면 Anchor가 자동으로 생성되기 때문. 단 Anchor에 대해 상세히 설정할 내용이 있다면 직접 Anchor를 선언해야한다.
export default function NavBar() {
    const router = useRouter()
    return (
        <nav>
            <Link href={"/"}>
                <a className={router.pathname === "/" ? "active" : ""}>Home</a>
            </Link>
            <Link href={"/about"}>
                About
            </Link>
            <Link href={"/counter"}>
                <a className={router.pathname === "/counter" ? "active" : ""}>Counter</a>
            </Link>
            <style jsx>{`
                nav {
                    background-color: tomato;
                }
                a {
                    text-decoration: none;
                }
                .active {
                    color: blue;
                }
            `}</style>
        </nav>
    )
}
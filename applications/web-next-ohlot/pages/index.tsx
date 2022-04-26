import Title from "../components/Title";
import WordBox from "../components/WordBox";
import DndLayout from "../components/DndLayout";
import CollisionObject from "../components/CollisionObject";

export default function Home() {
    return (
        <div>
            <Title title={"Home"}/>
            <CollisionObject/>
            <CollisionObject/>
            <CollisionObject/>
            <CollisionObject/>
        </div>
    )
}
import {DndProvider} from "react-dnd";
import {HTML5Backend} from "react-dnd-html5-backend";

type DndLayoutProps = {
    children: any
}

export default function DndLayout(props:DndLayoutProps) {
    return (
        <>
            <DndProvider backend={HTML5Backend}>
                {props.children}
            </DndProvider>
        </>
    )
}
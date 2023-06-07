import { Table } from '@nextui-org/react';
import { useSSR } from '@nextui-org/react'

export default function TableTransaction() {
    const { isBrowser } = useSSR()
    const columns = [
        {
            key: "category",
            label: "CATEGORY",
        },
        {
            key: "date",
            label: "DATE",
        },
        {
            key: "amount",
            label: "AMOUNT",
        },
    ];
    const rows = [
        {
            key: "1",
            category: "Education",
            date: "Jan 20, 2021",
            amount: "200.000",
        },
        {
            key: "2",
            category: "Education",
            date: "Jan 20, 2021",
            amount: "200.000",
        },
        {
            key: "3",
            category: "Education",
            date: "Jan 20, 2021",
            amount: "200.000",
        },
        {
            key: "4",
            category: "Education",
            date: "Jan 20, 2021",
            amount: "200.000",
        },
    ];
    return (
        <Table
            aria-label="Example table with dynamic content"
            css={{
                height: "auto",
                minWidth: "100%",
            }}
        >
            <Table.Header columns={columns}>
                {(column) => (
                    <Table.Column key={column.key}>{column.label}</Table.Column>
                )}
            </Table.Header>
            <Table.Body items={rows}>
                {(item) => (
                    <Table.Row key={item.key}>
                        {(columnKey) => <Table.Cell>{item[columnKey]}</Table.Cell>}
                    </Table.Row>
                )}
            </Table.Body>
        </Table>
    );
}
